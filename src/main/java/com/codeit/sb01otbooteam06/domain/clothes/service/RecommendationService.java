package com.codeit.sb01otbooteam06.domain.clothes.service;


import com.codeit.sb01otbooteam06.domain.auth.service.AuthService;
import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import com.codeit.sb01otbooteam06.domain.clothes.entity.ClothesAttribute;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesDto;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.OotdDto;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.RecommendationDto;
import com.codeit.sb01otbooteam06.domain.clothes.mapper.ClothesMapper;
import com.codeit.sb01otbooteam06.domain.clothes.repository.ClothesAttributeRepository;
import com.codeit.sb01otbooteam06.domain.clothes.repository.ClothesRepository;
import com.codeit.sb01otbooteam06.domain.clothes.utils.ClothesUtils;
import com.codeit.sb01otbooteam06.domain.profile.entity.Profile;
import com.codeit.sb01otbooteam06.domain.profile.exception.ProfileNotFoundException;
import com.codeit.sb01otbooteam06.domain.profile.repository.ProfileRepository;
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.user.exception.UserNotFoundException;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
import com.codeit.sb01otbooteam06.domain.weather.entity.Weather;
import com.codeit.sb01otbooteam06.domain.weather.exception.WeatherNotFoundException;
import com.codeit.sb01otbooteam06.domain.weather.repository.WeatherRepository;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.ThinkingConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RecommendationService {

  private final ClothesService clothesService;
  private final AuthService authService;

  //todo: 서비스? 웨더 확인해보고 추후 변경
  private final WeatherRepository weatherRepository;
  private final UserRepository userRepository;
  private final ProfileRepository profileRepository;

  private final ClothesMapper clothesMapper;
  private final ClothesRepository clothesRepository;

  private final ClothesUtils clothesUtils;
  private final ClothesAttributeRepository clothesAttributeRepository;

  //TODO: 의상 추천 알고리즘
  //날씨 데이터, 사용자가 등록한 의상, 프로필 정보를 활용하여 의상을 추천
  @Transactional
  public RecommendationDto create(UUID weatherId) {

    /** 날씨 추천 로직
     * 1. 날씨 데이터 가져옴
     * 2. gen-ai에게 프롬포팅해서 결과값(의상 속성중 날씨 관련)을 수치화
     * 3. 해당 수치를 토대로 의상리스트에서, 속성에서 값이 높은것들 산출
     * 4. 의상 추천 조합 생성해 DB저장
     *
     * TODO: 1)프로필 위치로, 배치 작업으로 DB저장해 꺼내써 빠른 사용자 속도 경험 제공,
     *       2)새로운 위치시 즉시호출
     */

    //현재 로그인한 유저 정보를 얻는다.
    UUID userId = authService.getCurrentUserId();
    User user = userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException(userId)
    );

    //추천에 필요한 데이터 가져오기
    double[] weatherData = getWeatherData(weatherId, userId);

    //의상 추천에 필요한 가중치 계산
    int[] recoWeight = getWeightByAi(weatherData);

    //todo: 추천의상 저장을 위한 테이블이 필요함.
    List<OotdDto> result = new ArrayList<>();

    //todo: 현재 임시 ootd반환이며 수정 필요.

    return new RecommendationDto(weatherId, userId, getFakeClothes(user));
  }

  //todo: 삭제
  private List<OotdDto> getFakeClothes(User user) {
    //페이크 데이터
    // 1. 의상 리스트 가져오기
    List<Clothes> clothesList = clothesRepository.findAllByOwner(user);

    // 2. 의상 ID 리스트 추출
    List<UUID> clothesIds = clothesList.stream()
        .map(Clothes::getId)
        .toList();

    // 3. 의상 속성들 한꺼번에 가져오기 (의상 리스트로)
    List<ClothesAttribute> clothesAttributes = clothesAttributeRepository.findByClothesIn(
        clothesList);

    // 4. 의상 ID별 속성 매핑 (Map<ClothesId, List<ClothesAttribute>>)
    Map<UUID, List<ClothesAttribute>> attributesByClothesId = clothesAttributes.stream()
        .collect(Collectors.groupingBy(attr -> attr.getClothes().getId()));

    // 5. ClothesDto 리스트 만들기
    List<ClothesDto> clothesDtoList = clothesList.stream()
        .map(clothes -> clothesUtils.makeClothesDto(
            clothes,
            attributesByClothesId.getOrDefault(clothes.getId(), Collections.emptyList())))
        .toList();

    // 6. OotdDto 리스트 만들기
    return clothesDtoList.stream()
        .map(OotdDto::toDto)
        .toList();

  }

  /**
   * 추천 의상 산출을 위한 유저와 날씨에 대한 정보를 반환합니다.
   *
   * @param weatherId
   * @return [달(월), 하늘상태, 온도, 습도, 풍속, 프로필 온도 민감도] double 배열
   */
  private double[] getWeatherData(UUID weatherId, UUID userId) {
    Weather weather = weatherRepository.findById(weatherId)
        .orElseThrow(() -> new WeatherNotFoundException());

    Profile profile = profileRepository.findById(userId)
        .orElseThrow(() -> new ProfileNotFoundException(userId));

    // 날씨 데이터에서 필요한 데이터를 배열로 저장
    double[] weatherData = new double[6];
    weatherData[0] = Double.parseDouble(weather.getForecastAt().toString().substring(5, 7)); //달(월)
    weatherData[1] = weather.getSkyStatus().getCode(); //하늘상태(구름정도)
    weatherData[2] = weather.getTemperature().getCurrent(); //현재 온도
    weatherData[3] = weather.getHumidity(); //습도
    weatherData[4] = weather.getWind().getSpeed(); //풍속
    weatherData[5] = profile.getTemperatureSensitivity(); //유저 프로필의 온도 민감도

    return weatherData;
  }

  /**
   * LLM api 를 통해 의상 추천 가중치를 계산합니다.
   *
   * @param weatherData
   * @return [계절, 두께감, 안감, 따뜻한 정도]
   */
  private int[] getWeightByAi(double[] weatherData) {
    //     * 의상 속성
//     * 1. 두께감, 계절,안감 ,따뜻한 정도

    //gen-ai 클라이언트
    Client client = new Client();

    long startTime = System.currentTimeMillis();

    //프롬프트
    //todo: 프롬프트를 env에서 관리하기(프롬프트 비공개)
    String prompt = String.format(
        "현재 %d 월이고, 구름 상태는(숫자가 클수록 흐림, 최대 4) %d, 온도는 %.1f, 습도는 %.1f, 풍속은 %.1f, 사람의 온도 민감도는 (0: 추위 많이 탐 ~ 5: 더위 많이 탐) %d 야. 이때 의상에 대한 두께감(0~3, 두꺼움~얇음), 계절(0~3, 봄~겨울), 안감(0~2, 없음~기모), 따뜻한 정도(0~1, 따뜻함~시원함)를 생각해서 콤마로 구분해서 정수숫자만 출력해",
        12, 1, weatherData[2], weatherData[3], weatherData[4],
        3);

    GenerateContentResponse response =
        client.models.generateContent(
            "gemini-2.5-flash",
            prompt,
            GenerateContentConfig.builder().
                thinkingConfig(
                    ThinkingConfig.builder().
                        thinkingBudget(0) //사고 예산을 0으로 설정해 속도 올림.
                        .build())
                .build()
        );

    long endTime = System.currentTimeMillis();

    System.out.println("response = " + response.text());
    System.out.println("응답 생성 시간: " + (endTime - startTime) + " ms");

    int[] weight = new int[4];

    return weight;
  }


}
