package com.codeit.sb01otbooteam06.domain.clothes.service;


import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.RecommendationDto;
import com.codeit.sb01otbooteam06.domain.weather.repository.WeatherRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationService {

  //서비스?
  private final WeatherRepository weatherRepository;

  public RecommendationDto create(UUID weatherId) {

    return null;
  }


}
