package com.codeit.sb01otbooteam06.domain.clothes.service;


import com.codeit.sb01otbooteam06.domain.auth.service.AuthService;
import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesCreateRequest;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.OotdDto;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.RecommendationDto;
import com.codeit.sb01otbooteam06.domain.clothes.mapper.ClothesMapper;
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
import com.codeit.sb01otbooteam06.domain.weather.repository.WeatherRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendationService {

  private final AuthService authService;

  private final ClothesService clothesService;

  //todo: 서비스? 웨더 확인해보고 추후 변경
  private final WeatherRepository weatherRepository;

  private final UserRepository userRepository;

  private final ClothesMapper clothesMapper;

  //TODO: 의상 추천 알고리즘
  //날씨 데이터, 사용자가 등록한 의상, 프로필 정보를 활용하여 의상을 추천
  @Transactional
  public RecommendationDto create(UUID weatherId) {

    UUID userId = authService.getCurrentUserId();

//    //todo: 유저 아이디 획득
//    User user = userRepository.findByEmail("admin@example.com")
//        .orElseThrow(() -> new NoSuchElementException());

    //날씨

    // 온도
//
//    //todo: 시간이 소요될 것으로 예상되어 우선 임시 데이터 던지게하기
//    List<OotdDto> result = new ArrayList<>();
//    result.add(makeDummyOotdDto("상의", "TOP"));
//    result.add(makeDummyOotdDto("하의", "BOTTOM"));
//    result.add(makeDummyOotdDto("아우터", "OUTER"));
//    result.add(makeDummyOotdDto("모자", "HAT"));
    return new RecommendationDto(weatherId, userId, List.of());
  }


  //admin의 더미 Ootd데이터 생성
  private OotdDto makeDummyOotdDto(String name, String type) {

    User user = userRepository.findByEmail("admin@example.com")
        .orElseThrow(() -> new NoSuchElementException());

    Clothes clothes = new Clothes(user, name, type, null);

    //의상 저장
    clothesService.create(new ClothesCreateRequest(
        user.getId(),
        name, type,
        Collections.emptyList()

    ), null);

    return OotdDto.toDto(clothesMapper.toDto(clothes));

  }

}
