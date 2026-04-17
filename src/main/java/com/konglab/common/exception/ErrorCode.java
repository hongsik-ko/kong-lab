package com.konglab.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 정의
 *
 * - HTTP 상태코드
 * - 내부 에러 코드
 * - 메시지
 *
 * 를 함께 관리한다.
 */
@Getter
public enum ErrorCode {
    // 공통
    COMMON_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_001", "서버 오류가 발생했습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_002", "잘못된 요청입니다."),

    // 도메인
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "STOCK_001", "존재하지 않는 종목입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
