package com.codeit.sb01saasteam06.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	//global
	INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
	INVALID_REQUEST("잘못된 요청입니다"),
	ILLEGAL_ARGUMENT_ERROR("잘못된 인수가 전달되었습니다.")



	;


	private final String message;

}
