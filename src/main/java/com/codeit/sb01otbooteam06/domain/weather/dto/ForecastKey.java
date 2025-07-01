package com.codeit.sb01otbooteam06.domain.weather.dto;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record ForecastKey(
    Instant baseInstant,
    Instant fcstInstant,
    int gridX, int gridY) {

    /* --------- 정적 팩토리 --------- */
    public static ForecastKey of(KmaVillageItem i) {      // ← public
        return new ForecastKey(
            toInstant(i.baseDate(), i.baseTime()),
            toInstant(i.fcstDate(), i.fcstTime()),
            i.nx(), i.ny());
    }

    /* --------- UUID 변환 --------- */
    public UUID toUuid() {                                // ← public
        String raw = baseInstant + "|" + fcstInstant + "|" + gridX + "|" + gridY;
        return UUID.nameUUIDFromBytes(raw.getBytes(StandardCharsets.UTF_8));
    }

    /* --------- 내부 파서 --------- */
    private static Instant toInstant(String yyyymmdd, String hhmm) {
        LocalDate d = LocalDate.parse(yyyymmdd, DateTimeFormatter.BASIC_ISO_DATE);
        // "600" 같은 값이 올 수 있으니 4자리로 왼쪽 0패딩
        String time = String.format("%04d", Integer.parseInt(hhmm));
        LocalTime t = LocalTime.parse(time, DateTimeFormatter.ofPattern("HHmm"));
        return ZonedDateTime.of(d, t, ZoneId.of("Asia/Seoul")).toInstant();
    }
}
