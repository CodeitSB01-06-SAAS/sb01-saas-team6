package com.codeit.sb01otbooteam06.domain.weather.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(staticName = "from")
public class Wind {
    private Double speed;      // WSD (m/s)
    private Integer level;     // 1·2·3  (optional)
    private Double direction;  // VEC  (0-360)
    private Double u;          // UUU
    private Double v;          // VVV
}
