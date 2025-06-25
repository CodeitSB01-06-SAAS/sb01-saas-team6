package com.codeit.sb01otbooteam06.domain.weather.entity;


import com.codeit.sb01otbooteam06.domain.base.BaseUpdatableEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "weathers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Weather extends BaseUpdatableEntity {

  private Instant forecastedAt;
  private Instant forecastAt;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "latitude", column = @Column(name = "lat")),
      @AttributeOverride(name = "longitude", column = @Column(name = "lon")),
      @AttributeOverride(name = "x", column = @Column(name = "grid_x")),
      @AttributeOverride(name = "y", column = @Column(name = "grid_y"))
  })
  private Location location;

  @Enumerated(EnumType.STRING)
  private SkyStatus skyStatus;

  // 강수
  private String precipitationType;     // PTY 코드
  private double precipitationAmount;   // RN1, PCP
  private double precipitationProbability; // POP

  // 습도
  private double humidityCurrent;
  private double humidityComparedToDayBefore;

  // 온도
  private double temperatureCurrent;
  private double temperatureComparedToDayBefore;
  private double temperatureMin;
  private double temperatureMax;

  // 풍속
  private double windSpeed;      // WSD
  private String windAsWord;     // 1:WEAK,2:MODERATE,3:STRONG

  /* ---- 관계 ---- */
  @OneToMany(mappedBy = "weather", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WeatherLocationName> locationNames = new ArrayList<>();

  private Weather(Instant forecastedAt,
      Instant forecastAt,
      Location location,
      SkyStatus skyStatus) {
    this.forecastedAt = forecastedAt;
    this.forecastAt = forecastAt;
    this.location = location;
    this.skyStatus = skyStatus;
  }

  /**
   * 필수 값만 받는 생성자
   */
  public static Weather from(Instant forecastedAt,
      Instant forecastAt, double lat, double lon, int x, int y, SkyStatus skyStatus) {
    return new Weather(forecastedAt, forecastAt, Location.of(lat, lon, x, y), skyStatus);
  }

  /**
   * 위치 이름 목록까지 한 번에 넣는 팩터리
   */
  public static Weather fromWithLocations(Instant forecastedAt,
      Instant forecastAt,
      double lat, double lon, int x, int y,
      SkyStatus skyStatus,
      List<String> locationNames) {

    Weather w = new Weather(
        forecastedAt,
        forecastAt,
        Location.of(lat, lon, x, y),
        skyStatus);

    locationNames.forEach(w::addLocationName);
    return w;
  }

  /* ---------- 편의 메서드 ---------- */
  public void addLocationName(String name) {
    WeatherLocationName ln = WeatherLocationName.from(this, name);
    this.locationNames.add(ln);   // 양방향 컬렉션 동기화
  }
}

