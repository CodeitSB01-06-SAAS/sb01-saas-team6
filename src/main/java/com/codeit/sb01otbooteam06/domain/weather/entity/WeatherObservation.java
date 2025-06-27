package com.codeit.sb01otbooteam06.domain.weather.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.Getter;

/* ===== WeatherObservation (초단기 실황) ===== */
@Entity
@Getter
@Table(name="weather_observation",
    indexes=@Index(columnList="observed_at,grid_x,grid_y"),
    uniqueConstraints=@UniqueConstraint(columnNames={"observed_at","grid_x","grid_y"}))
public class WeatherObservation extends AbstractWeather {

    @Column(name="observed_at", nullable=false)
    private Instant observedAt;           // baseDate+baseTime

    protected WeatherObservation(){}      // JPA

    private WeatherObservation(Instant obs, Location loc){
        this.observedAt = obs;
        this.location   = loc;
    }
    public static WeatherObservation from(Instant obs, Location loc){
        return new WeatherObservation(obs, loc);
    }
}
