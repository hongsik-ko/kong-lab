package com.konglab.common.response;

import lombok.Getter;

/**
 * 공통 실패 응답 포맷
 *
 * 예:
 * {
 *   "success": false,
 *   "code": "STOCK_NOT_FOUND",
 *   "message": "존재하지 않는 종목입니다."
 * }
 */
@Getter
public class ApiErrorResponse {
    private final boolean success;
    private final String code;
    private final String message;

    public ApiErrorResponse(String code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }
}
