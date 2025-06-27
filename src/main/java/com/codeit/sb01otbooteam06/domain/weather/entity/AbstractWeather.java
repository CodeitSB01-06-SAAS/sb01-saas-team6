package com.codeit.sb01otbooteam06.domain.weather.entity;

import com.codeit.sb01otbooteam06.domain.base.BaseUpdatableEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class AbstractWeather extends BaseUpdatableEntity {

    /* 좌표  */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="latitude",  column=@Column(name="lat")),
        @AttributeOverride(name="longitude", column=@Column(name="lon")),
        @AttributeOverride(name="x", column=@Column(name="grid_x")),
        @AttributeOverride(name="y", column=@Column(name="grid_y"))
    })
    protected Location location;

    /*  공통 기상 값 */
    @Enumerated(EnumType.STRING) protected SkyStatus sky;   // SKY
    @Enumerated(EnumType.STRING) protected PrecipType pty;  // PTY
    @Embedded protected Wind wind;                         // UUU/VVV/VEC/WSD

    protected Double temperature;          // T1H/TMP
    protected Double temperatureMin;       // TMN (forecast)
    protected Double temperatureMax;       // TMX (forecast)
    protected Double humidity;             // REH
    protected Double precipAmount;         // RN1/PCP
    protected String precipAmountText;    // "1 mm 미만" 등
    protected Double precipProbability;    // POP (forecast)
    protected Double snowAmount;           // SNO
    protected Double lightning;            // LGT
}
