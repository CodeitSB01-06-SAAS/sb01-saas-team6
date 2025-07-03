package com.codeit.sb01otbooteam06.domain.weather.service;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

import com.codeit.sb01otbooteam06.domain.weather.dto.ForecastKey;
import com.codeit.sb01otbooteam06.domain.weather.dto.KmaVillageResponse;
import com.codeit.sb01otbooteam06.domain.weather.dto.KmaVillageItem;
import com.codeit.sb01otbooteam06.domain.weather.dto.WeatherAPILocationDto;
import com.codeit.sb01otbooteam06.domain.weather.dto.WeatherDto;
import com.codeit.sb01otbooteam06.domain.weather.dto.WeatherMetrics;
import com.codeit.sb01otbooteam06.domain.weather.entity.Location;
import com.codeit.sb01otbooteam06.domain.weather.entity.Temperature;
import com.codeit.sb01otbooteam06.domain.weather.entity.Weather;
import com.codeit.sb01otbooteam06.domain.weather.exception.WeatherNotFoundException;
import com.codeit.sb01otbooteam06.domain.weather.mapper.WeatherMapper;
import com.codeit.sb01otbooteam06.domain.weather.repository.WeatherRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final KmaApiClient kmaApiClient;
    private final KakaoLocalClient kakaoLocalClient;
    private final CoordinateConverter coordinateConverter;

    /* 위치 조회 */
    public WeatherAPILocationDto findLocation(double lat, double lon) {

        /* 1) DB에서 해당 위치의 이름명이 있는지 확인 */
        return weatherRepository.latestLocation(lat, lon)
            .map(loc -> toLocationRes(loc, kakaoLocalClient.coordToRegion(lat, lon)))

            /* 2) DB에 만약 없을시 → (lat,lon) 직접 변환 & Kakao 호출 */
            .orElseGet(() -> {

                // 위‧경도 → 격자 변환
                CoordinateConverter.Grid grid =
                    coordinateConverter.latLonToGrid(lat, lon);

                // 행정동명 실시간 조회
                List<String> names =
                    kakaoLocalClient.coordToRegion(lat, lon);

                // 응답 DTO
                return new WeatherAPILocationDto(lat, lon, grid.gridX(), grid.gridY(), names);
            });
    }

    /* -------------- 최신 예보 번들 -------------- */
    public List<WeatherDto> findLatestBundle(double lat, double lon) {

        List<Weather> bundle = weatherRepository.findForecastBundle(lat, lon);
        if (bundle.isEmpty()) {
            throw new WeatherNotFoundException().withLatLon(lat, lon);
        }

        fillMinMax(bundle);   // ← min/max null 보간

        /* 전일 대비용 맵 (전일 같은 forecastAt) */
        Map<Instant, Weather> yMap = bundle.stream()
            .collect(Collectors.toMap(Weather::getForecastAt, w -> w));

        return bundle.stream()
            .map(w -> toWeatherRes(w,
                yMap.get(w.getForecastAt().minus(1, DAYS))))
            .toList();
    }

    /**
     * 단기예보 “getVilageFcst”를 호출해 Weather 엔티티를 저장한다.
     *
     * @param lat 위도
     * @param lon 경도
     */
    public void saveVillageForecast(double lat, double lon) {

        /* 1) (lat,lon) → (nx,ny) 변환 */
        CoordinateConverter.Grid grid = coordinateConverter.latLonToGrid(lat, lon);

        /* 2) KMA 단기예보 호출 */
        KmaVillageResponse dto = kmaApiClient.getVillageFcst(grid.gridX(), grid.gridY());
        if (dto.items().isEmpty()) {
            return;   // 데이터가 없으면 조용히 종료
        }

        /* 3) (baseDate+baseTime, fcstDate+fcstTime) 단위로 그룹핑 */
        Map<ForecastKey, List<KmaVillageItem>> grouped =
            dto.items().stream()
                .collect(Collectors.groupingBy(ForecastKey::of));

        /* 4) 각 그룹 → Weather 엔티티 변환·저장 */
        grouped.forEach((key, items) -> {
            // 4-1) 이미 존재하면 *(update) / 없으면 new 생성*
            Weather weather = weatherRepository.findById(key.toUuid())
                .orElseGet(() -> Weather.from(
                    key.baseInstant(), key.fcstInstant(),
                    Location.from(lat, lon, grid.gridX(), grid.gridY())));

            // 4-2) KMA category → Embedded 값 매핑
            WeatherMetrics metrics = WeatherMapper.toMetrics(items);
            weather.applyMetrics(
                metrics.skyStatus(),
                metrics.ptyType(),
                metrics.temperature(),
                metrics.precipitation(),
                metrics.wind(),
                metrics.humidity(),
                metrics.snow(),
                metrics.lightning());

            // 4-3) 행정동 이름이 없으면 한 번만 붙인다.
            if (weather.getLocationNames().isEmpty()) {
                kakaoLocalClient.coordToRegion(lat, lon)
                    .forEach(weather::addLocationName);
            }

            weatherRepository.save(weather);
        });
    }

    /* private  */
    private static WeatherAPILocationDto toLocationRes(
        Location loc, List<String> names) {
        return new WeatherAPILocationDto(
            loc.getLatitude(), loc.getLongitude(),
            loc.getX(), loc.getY(), names);
    }

    private static WeatherDto toWeatherRes(Weather w, Weather yDay) {

        double curT = ofNullable(w.getTemperature().getCurrent()).orElse(0.0);
        Double yT = yDay == null ? null : yDay.getTemperature().getCurrent();

        double curH = ofNullable(w.getHumidity()).orElse(0.0);
        Double yH = yDay == null ? null : yDay.getHumidity();

        WeatherDto.Temperature tDto = new WeatherDto.Temperature(
            curT, yT == null ? null : curT - yT,
            w.getTemperature().getMin(),
            w.getTemperature().getMax());

        WeatherDto.Humidity hDto = new WeatherDto.Humidity(
            curH, yH == null ? null : curH - yH);

        WeatherDto.Precipitation pDto = toPrecip(w);

        double spd = ofNullable(w.getWind().getSpeed()).orElse(0.0);
        String word = spd < 4 ? "WEAK" : spd < 9 ? "MODERATE" : "STRONG";
        WeatherDto.WindSpeed wsDto = new WeatherDto.WindSpeed(spd, word);

        Location l = w.getLocation();
        WeatherDto.Loc locDto = new WeatherDto.Loc(
            l.getLatitude(), l.getLongitude(),
            l.getX(), l.getY(), null);

        return new WeatherDto(
            w.getId(),
            w.getForecastedAt().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            w.getForecastAt().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            locDto, w.getSkyStatus(),
            pDto, hDto, tDto, wsDto);
    }

    private static WeatherDto.Precipitation toPrecip(Weather w) {
        if (w.getPrecipitation() == null) {
            return new WeatherDto.Precipitation(
                w.getPrecipitationType(), 0.0, 0.0);
        }
        return new WeatherDto.Precipitation(
            w.getPrecipitationType(),
            ofNullable(w.getPrecipitation().getAmount()).orElse(0.0),
            ofNullable(w.getPrecipitation().getProbability()).orElse(0.0));
    }

    /* ---------- min/max 채우기 ---------- */
    private static void fillMinMax(List<Weather> bundle) {

        Double min = bundle.stream()
            .map(w -> w.getTemperature().getMin())
            .filter(Objects::nonNull)
            .min(Double::compare).orElse(null);

        Double max = bundle.stream()
            .map(w -> w.getTemperature().getMax())
            .filter(Objects::nonNull)
            .max(Double::compare).orElse(null);

        if (min == null && max == null) {
            return; // 둘 다 없으면 끝
        }

        bundle.forEach(w -> {
            Temperature old = w.getTemperature();
            Temperature filled = Temperature.from(
                old.getCurrent(),      // current 유지
                min,                   // 공통 min
                max);                  // 공통 max
            w.applyMetrics(
                w.getSkyStatus(),
                w.getPrecipitationType(),
                filled,
                w.getPrecipitation(),
                w.getWind(),
                w.getHumidity(),
                w.getSnowAmount(),
                w.getLightning());
        });
    }
}
