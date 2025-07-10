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
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.user.exception.UserNotFoundException;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
import com.codeit.sb01otbooteam06.domain.weather.repository.WeatherRepository;
import com.google.genai.Client;
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

  private final ClothesMapper clothesMapper;
  private final ClothesRepository clothesRepository;

  private final ClothesUtils clothesUtils;
  private final ClothesAttributeRepository clothesAttributeRepository;

  //TODO: 의상 추천 알고리즘
  //날씨 데이터, 사용자가 등록한 의상, 프로필 정보를 활용하여 의상을 추천
  @Transactional
  public RecommendationDto create(UUID weatherId) {

    //현재 로그인한 유저의 아이디를 획득한다.
    UUID userId = authService.getCurrentUserId();

    User user = userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException(userId)
    );

    /**날씨 데이터
     * 1. 기온, 바람 , 습도, 날씨, 계절
     *
     * 의상 속성
     * 1. 두께감, 계절,안감 ,따뜻한 정도
     * */

    //TODO날씨 데이터 생기면 열기
//    //날씨 데이터 가져오기
//    Weather weather = weatherRepository.findById(weatherId)
//        .orElseThrow(() -> new WeatherNotFoundException());
//
//    //날씨 데이터에서 필요한 것 추출
//    // humidity, temperature, windSpeed
//    List<Long> weatherThings = new ArrayList<>();
//    String skyStatus = String.valueOf(weather.getSkyStatus());

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
    List<OotdDto> ootdDtoList = clothesDtoList.stream()
        .map(OotdDto::toDto)
        .toList();

    ///날씨 추천 로직
    /**
     * 1. 날씨 데이터 가져옴
     * 2. gen-ai에게 프롬포팅해서 결과값(의상 속성중 날씨 관련)을 수치화
     * 3. 해당 수치를 토대로 의상리스트에서, 속성에서 값이 높은것들 산출
     * 4. 의상 추천 조합 생성해 DB저장
     *
     * TODO: 1)프로필 위치로, 배치 작업으로 DB저장해 꺼내써 빠른 사용자 속도 경험 제공,
     *       2)새로운 위치시 즉시호출
     */

    //gen-ai 클라이언트
    Client client = new Client();

//    long startTime = System.currentTimeMillis();
//
//    //todo: 생각 0으로 설정.
//    GenerateContentResponse response =
//        client.models.generateContent(
//            "gemini-2.5-flash",
//            //todo: text를 env에서 관리하기(프롬프트 비공개)
//            "프롬프트 작성",
//            null
//        );
//
//    long endTime = System.currentTimeMillis();
//
//    System.out.println("response = " + response.text());
//    System.out.println("응답 생성 시간: " + (endTime - startTime) + " ms");

    //todo: 추천의상 저장을 위한 테이블이 필요함.
    List<OotdDto> result = new ArrayList<>();

    return new RecommendationDto(weatherId, userId, ootdDtoList);
  }

  private List<Integer> weightByAi(List<Integer> weatherThings, String skyStatus) {

    //todo: 배열? 길이가 고정이기 때문에.
    List<Integer> values = new ArrayList<>();

    return values;
  }


}
