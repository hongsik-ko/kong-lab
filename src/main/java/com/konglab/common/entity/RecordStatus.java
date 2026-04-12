package com.konglab.common.entity;

import lombok.Getter;
/*
*  레코드 상태값 정의 (정상 / 삭제 / 비활성화 등등)
*  DB에는 char(1)로 저장
*
* */
@Getter
public enum RecordStatus {
    N("Nomal"), // default.
    D("Deleted"), // This it deleted Content.(you can't find it.)
    H("Hidden"), // This is Hidden content.
    E("Error"); // Error that caused by system.

    private final String description;

    RecordStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
