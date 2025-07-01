package com.codeit.sb01otbooteam06.domain.auth.service;

import com.codeit.sb01otbooteam06.domain.auth.dto.ResetPasswordRequest;
import com.codeit.sb01otbooteam06.domain.auth.dto.SignInRequest;
import com.codeit.sb01otbooteam06.domain.auth.dto.TokenResponse;

public interface AuthService {

    /**
     * 로그인 - accessToken, refreshToken 발급
     */
    TokenResponse signIn(SignInRequest request);

    /**
     * 비밀번호 초기화 (임시 비밀번호 발급)
     */
    void resetPassword(ResetPasswordRequest request);

    /**
     * 리프레시 토큰으로 액세스 토큰 재발급
     */
    String refreshAccessToken(String refreshToken);

    /**
     * 리프레시 토큰으로 현재 액세스 토큰 조회
     */
    String getAccessToken(String refreshToken);
}
