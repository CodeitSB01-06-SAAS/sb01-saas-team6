package com.codeit.sb01otbooteam06.domain.auth.controller;

import com.codeit.sb01otbooteam06.domain.auth.dto.ResetPasswordRequest;
import com.codeit.sb01otbooteam06.domain.auth.dto.SignInRequest;
import com.codeit.sb01otbooteam06.domain.auth.dto.TokenResponse;
import com.codeit.sb01otbooteam06.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 API 컨트롤러 (Swagger 명세 기준 완성본)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인 - accessToken 반환, refreshToken 쿠키에 저장
     */
    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInRequest request,
                                         HttpServletResponse response) {
        TokenResponse tokenResponse = authService.signIn(request);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false) // 배포 시 true로 변경 필요
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok(tokenResponse.getAccessToken());
    }

    /**
     * 비밀번호 초기화 (임시 비밀번호 발급)
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

    /**
     * 로그아웃 - refreshToken 쿠키 제거
     */
    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.noContent().build();
    }

    /**
     * 토큰 재발급 - refreshToken으로 새로운 accessToken 발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }

    /**
     * 엑세스 토큰 조회 - refreshToken으로 accessToken 반환
     */
    @GetMapping("/me")
    public ResponseEntity<String> getAccessToken(@CookieValue("refresh_token") String refreshToken) {
        String accessToken = authService.getAccessToken(refreshToken);
        return ResponseEntity.ok(accessToken);
    }
}
