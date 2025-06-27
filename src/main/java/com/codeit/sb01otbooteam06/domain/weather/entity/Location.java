package com.codeit.sb01otbooteam06.domain.weather.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "from")   // 편의용
public class Location {

  @Column(nullable = false)
  private Double latitude;
  @Column(nullable = false)
  private Double longitude;

  @Column(name = "grid_x", nullable = false)
  private Integer x;
  @Column(name = "grid_y", nullable = false)
  private Integer y;
}
