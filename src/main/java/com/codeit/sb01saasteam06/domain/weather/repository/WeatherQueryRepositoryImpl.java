package com.codeit.sb01saasteam06.domain.weather.repository;

import com.codeit.sb01saasteam06.domain.weather.entity.Location;
import com.codeit.sb01saasteam06.domain.weather.entity.QWeather;
import com.codeit.sb01saasteam06.domain.weather.entity.QWeatherLocationName;
import com.codeit.sb01saasteam06.domain.weather.entity.Weather;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeatherQueryRepositoryImpl implements WeatherQueryRepository {

    private final JPAQueryFactory q;
    private static final QWeather w = QWeather.weather;
    private static final QWeatherLocationName ln = QWeatherLocationName.weatherLocationName;

    @Override
    public List<Weather> findForecastBundle(double lat, double lon) {

        // 1. 최신 forecastedAt 1건만 먼저 찾기
        Instant latestForecastedAt = q.select(w.forecastedAt.max())
            .from(w)
            .where(w.location.latitude.eq(lat)
                .and(w.location.longitude.eq(lon)))
            .fetchFirst();

        if (latestForecastedAt == null) {
            return List.of();
        }

        // 2. 해당 시점의 forecastAt 행 전체 조회 (LocationName fetchJoin)
        return q.selectFrom(w)
            .leftJoin(w.locationNames, ln).fetchJoin()
            .where(w.location.latitude.eq(lat)
                .and(w.location.longitude.eq(lon))
                .and(w.forecastedAt.eq(latestForecastedAt)))
            .orderBy(w.forecastAt.desc())     // 최신 예보시각순
            .fetch();
    }

    // 2. 위치만 반환
    @Override
    public Optional<Location> latestLocation(double lat, double lon) {

        Location loc = q.select(w.location)
            .from(w)
            .where(w.location.latitude.eq(lat)
                .and(w.location.longitude.eq(lon)))
            .orderBy(w.forecastedAt.desc())
            .fetchFirst();

        return Optional.ofNullable(loc);
    }

	/*@Override
	public List<Weather> findForecastBundle(double lat, double lon) {
		return List.of();
	}

	@Override
	public Optional<Location> latestLocation(double lat, double lon) {
		return Optional.empty();
	}*/
}