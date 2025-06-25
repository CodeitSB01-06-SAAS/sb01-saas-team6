package com.codeit.sb01otbooteam06.domain.weather.entity;

import com.codeit.sb01otbooteam06.domain.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "weather_location_names")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeatherLocationName extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "weather_id", nullable = false)
  private Weather weather;

  private String locationName;

  private WeatherLocationName(Weather weather, String locationName) {
    this.weather = weather;
    this.locationName = locationName;
  }

  public static WeatherLocationName from(Weather weather, String locationName) {
    return new WeatherLocationName(weather, locationName);
  }
}
