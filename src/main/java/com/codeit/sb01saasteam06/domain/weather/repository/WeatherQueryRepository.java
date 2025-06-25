package com.codeit.sb01saasteam06.domain.weather.repository;

import com.codeit.sb01saasteam06.domain.weather.entity.Weather;
import java.util.Optional;

public interface WeatherQueryRepository {
    Optional<Weather> latest(double lat, double lon);
}
