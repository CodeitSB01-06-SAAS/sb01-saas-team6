package com.codeit.sb01otbooteam06.domain.clothes.service;


import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.RecommendationDto;
import com.codeit.sb01otbooteam06.domain.weather.repository.WeatherRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendationService {

  //todo: 서비스? 웨더 확인해보고 추후 변경
  private final WeatherRepository weatherRepository;

  //TODO: 의상 추천 알고리즘
  //날씨 데이터, 사용자가 등록한 의상, 프로필 정보를 활용하여 의상을 추천
  @Transactional
  public RecommendationDto create(UUID weatherId) {

    //todo: 유저 아이디 획득
    UUID userId = UUID.randomUUID();

    //todo: 시간이 소요될 것으로 예상되어 우선 임시 데이터 던지게하기
    return null;
  }


}
