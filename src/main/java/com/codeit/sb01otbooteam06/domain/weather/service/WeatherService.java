package com.codeit.sb01otbooteam06.domain.weather.service;

import com.codeit.sb01otbooteam06.domain.weather.dto.ForecastKey;
import com.codeit.sb01otbooteam06.domain.weather.dto.KmaVillageResponse;
import com.codeit.sb01otbooteam06.domain.weather.dto.KmaVillageItem;
import com.codeit.sb01otbooteam06.domain.weather.dto.WeatherMetrics;
import com.codeit.sb01otbooteam06.domain.weather.entity.Location;
import com.codeit.sb01otbooteam06.domain.weather.entity.Weather;
import com.codeit.sb01otbooteam06.domain.weather.mapper.WeatherMapper;
import com.codeit.sb01otbooteam06.domain.weather.repository.WeatherRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final KmaApiClient kmaApiClient;           // getVilageFcst 호출용
    private final KakaoLocalClient kakaoLocalClient;   // 행정동 이름 조회
    private final CoordinateConverter coordinateConverter; // lat/lon ↔ nx/ny

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
        if (dto.items().isEmpty()) return;   // 데이터가 없으면 조용히 종료

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
}
