package com.konglab.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 엔티티가 상속받는 기본 엔티티
 *
 * 역할:
 * - 생성/수정 시간 관리 (UTC 기준)
 * - 생성자/수정자 관리
 * - 레코드 상태 관리
 */
@Getter
@MappedSuperclass // 공통 필드만 상속, 테이블은 생성하지 않음
@EntityListeners(AuditingEntityListener.class) // JPA Audit 동작
public class BasicEntity {
    /**
     * 생성일시 (UTC 기준)
     * insert 시 자동 생성, 이후 변경 불가
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성일시 (UTC)")
    private LocalDateTime createdAt;

    /**
     * 생성자 (로그인 사용자 ID)
     * insert시 현재 로그인 사용자 ID 자동 입력
     */
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    @Comment("생성자 ID")
    private Long createdBy;

    /**
     * 수정일시 (UTC 기준)
     * update 시 자동 갱신
     */
    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    @Comment("수정일시 (UTC)")
    private LocalDateTime modifiedAt;

    /**
     * 수정자 (로그인 사용자 ID)
     * update 시 현재 로그인 사용자 ID 자동 입력
     */
    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    @Comment("수정자 ID")
    private Long modifiedBy;

    /**
     * 레코드 상태
     * N: 정상, D: 삭제, H: 숨김, A: 에러
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "record_status", nullable = false, length = 1)
    @Comment("레코드 상태")
    private RecordStatus recordStatus = RecordStatus.N;

    /**
     * 상태 변경 (삭제/숨김 등)
     */
    protected void changeStatus(RecordStatus status) {
        this.recordStatus = status;
    }
    /**
     * 레코드 삭제 상태 변경
     */
    public void markDeleted() {
        this.recordStatus = RecordStatus.D;
    }

    /**
     * 레코드 숨김 상태 변경
     */
    public void markHidden() {
        this.recordStatus = RecordStatus.H;
    }

    /**
     * 레코드 정상 상태 변경
     */
    public void markNormal() {
        this.recordStatus = RecordStatus.N;
    }

    /**
     * 레코드 에러 상태 변경
     */
    public void markError() {
        this.recordStatus = RecordStatus.E;
    }

}
