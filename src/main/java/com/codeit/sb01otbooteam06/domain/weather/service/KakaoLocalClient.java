package com.codeit.sb01otbooteam06.domain.weather.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoLocalClient {

    @Value("${kakao.rest-key}")        // KakaoAK {REST_API_KEY}
    private String kakaoKey;

    private final WebClient webClient;

    public List<String> coordToRegion(double lat, double lon) {

        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host("dapi.kakao.com")
                .path("/v2/local/geo/coord2regioncode.json")
                .queryParam("x", lon)
                .queryParam("y", lat)
                .build())
            .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoKey)
            .retrieve()
            .bodyToMono(KakaoDto.class)
            .map(dto -> dto.documents().stream()
                .filter(d -> "B".equals(d.region_type()))  // 법정동만
                .map(KakaoDoc::address_name)
                .toList())
            .block();
    }

    /* ---------- 응답 DTO ---------- */
    private record KakaoDto(List<KakaoDoc> documents) { }
    private record KakaoDoc(String region_type, String address_name) { }
}
