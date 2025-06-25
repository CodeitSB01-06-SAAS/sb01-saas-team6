package com.codeit.sb01saasteam06.domain.weather.entity;

/**
 * KMA SKY(하늘 상태) 코드 매핑 1 : 맑음(CLEAR) 3 : 구름 많음(MOSTLY_CLOUDY) 4 : 흐림(CLOUDY)
 */
public enum SkyStatus {
    CLEAR(1),
    MOSTLY_CLOUDY(3),
    CLOUDY(4);

    private final int code;

    SkyStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /**
     * KMA 코드(1·3·4) → Enum 매핑
     */
    public static SkyStatus fromCode(int code) {
        return switch (code) {
            case 1 -> CLEAR;
            case 3 -> MOSTLY_CLOUDY;
            case 4 -> CLOUDY;
            default -> throw new IllegalArgumentException("Unknown SKY code: " + code);
        };
    }
}
