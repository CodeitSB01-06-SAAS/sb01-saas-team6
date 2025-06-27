package com.codeit.sb01otbooteam06.domain.weather.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/* ===== WeatherForecast (초단기·단기 예보) ===== */
@Entity
@Getter
@Table(name="weather_forecast",
    uniqueConstraints=@UniqueConstraint(
        columnNames={"forecasted_at","forecast_at","grid_x","grid_y"}))
public class WeatherForecast extends AbstractWeather {

    @Column(nullable=false) private Instant forecastedAt;  // 발표(base)
    @Column(nullable=false) private Instant forecastAt;    // 예보(fcst)

    /* ---- 위치 이름 FK ---- */
    @OneToMany(mappedBy="forecast", cascade= CascadeType.ALL, orphanRemoval=true)
    private List<WeatherLocationName> locationNames = new ArrayList<>();

    protected WeatherForecast(){}       // JPA

    private WeatherForecast(Instant base, Instant fcst, Location loc){
        this.forecastedAt = base;
        this.forecastAt   = fcst;
        this.location     = loc;
    }
    public static WeatherForecast of(Instant base, Instant fcst, Location loc){
        return new WeatherForecast(base, fcst, loc);
    }
    public void addLocationName(String name){
        locationNames.add(WeatherLocationName.from(this, name));
    }
}