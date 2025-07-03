package com.codeit.sb01otbooteam06.domain.weather.repository;

import com.codeit.sb01otbooteam06.domain.weather.entity.Location;
import com.codeit.sb01otbooteam06.domain.weather.entity.QWeather;
import com.codeit.sb01otbooteam06.domain.weather.entity.QWeatherLocationName;
import com.codeit.sb01otbooteam06.domain.weather.entity.Weather;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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

    // 소수점 3자리까지 비교용
    final double TOL = 0.0005;

    // 1단계: 최신 forecastedAt 찾기
    Instant latestForecastedAt = q.select(w.forecastedAt.max())
        .from(w)
        .where(w.location.latitude.between(lat - TOL, lat + TOL)
            .and(w.location.longitude.between(lon - TOL, lon + TOL)))
        .fetchFirst();

    if (latestForecastedAt == null) {
      return List.of();
    }

    // 00시(KST) + 같은 좌표 묶음 조회
    NumberExpression<Integer> hour = Expressions.numberTemplate(
        Integer.class,
        "date_part('hour', timezone('Asia/Seoul', {0}))",
        w.forecastAt);

    return q.selectFrom(w)
        .leftJoin(w.locationNames, ln).fetchJoin()
        .where(w.location.latitude.between(lat - TOL, lat + TOL)
            .and(w.location.longitude.between(lon - TOL, lon + TOL))
            .and(w.forecastedAt.eq(latestForecastedAt))
            .and(hour.eq(0)))
        .orderBy(w.forecastAt.asc())
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