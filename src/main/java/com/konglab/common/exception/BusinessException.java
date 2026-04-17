package com.konglab.common.exception;
/**
 * 비즈니스 예외
 *
 * 서비스 로직에서 발생하는 예외는
 * 이 클래스를 통해 던진다.
 *
 * 예:
 * throw new BusinessException(ErrorCode.STOCK_NOT_FOUND);
 */
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 기본 메시지 설정
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
