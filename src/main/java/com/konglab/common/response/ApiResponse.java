package com.konglab.common.response;

import lombok.Getter;

/**
 * 공통 성공 응답 포맷
 *
 * 모든 정상 응답은 이 구조로 통일한다.
 *
 * 예:
 * {
 *   "success": true,
 *   "data": ...
 * }
 */
@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T data;

    private ApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    /**
     * 성공 응답 생성
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data);
    }
}
