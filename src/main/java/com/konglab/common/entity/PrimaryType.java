package com.konglab.common.entity;

import lombok.Getter;

/**
 * 대표 뉴스 유형
 *
 * P: 긍정 대표 뉴스
 * N: 부정 대표 뉴스
 */

@Getter
public enum PrimaryType {
    P("긍정 대표"),
    N("부정 대표");

    private final String description;

    PrimaryType(String description) {
        this.description = description;
    }
}
