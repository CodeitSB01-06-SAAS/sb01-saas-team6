package com.codeit.sb01otbooteam06.domain.clothes.service;


import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.RecommendationDto;
import com.codeit.sb01otbooteam06.domain.weather.repository.WeatherRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationService {

  //todo: 서비스? 웨더 확인해보고 추후 변경
  private final WeatherRepository weatherRepository;

  //TODO: 의상 추천 알고리즘
  /**
   * Generates a clothing recommendation based on the specified weather data.
   *
   * @param weatherId the unique identifier of the weather data to use for generating recommendations
   * @return a {@code RecommendationDto} containing the recommended clothing, or {@code null} if not implemented
   */
  public RecommendationDto create(UUID weatherId) {

    //todo: 유저 아이디 획득
    UUID userId = UUID.randomUUID();

    return null;
  }


}
