package com.codeit.sb01saasteam06.domain.weather.repository;


import com.codeit.sb01saasteam06.domain.weather.entity.Weather;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeatherQueryRepositoryImpl implements WeatherQueryRepository {

    private final JPAQueryFactory q;
    private static final QWeather w = QWeather.weather;

    @Override
    public Optional<Weather> latest(double lat, double lon) {
        return Optional.ofNullable(
            q.selectFrom(w)
                .where(w.latitude.eq(lat)
                    .and(w.longitude.eq(lon)))
                .orderBy(w.forecastAt.desc(), w.forecastedAt.desc())
                .fetchFirst()
        );
    }

}
