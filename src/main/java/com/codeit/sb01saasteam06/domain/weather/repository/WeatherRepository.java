package com.codeit.sb01saasteam06.domain.weather.repository;

import com.codeit.sb01saasteam06.domain.weather.entity.Weather;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, UUID>, WeatherQueryRepository {

}

