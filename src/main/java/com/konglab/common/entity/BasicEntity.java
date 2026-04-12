package com.konglab.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
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
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
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
     */
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
     */
    @Column(name = "modified_by", nullable = false)
    @Comment("수정자 ID")
    private Long modifiedBy;

    /**
     * 레코드 상태
     * N: 정상, D: 삭제, H: 숨김, A: 보관
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
     * 감사 정보 업데이트
     * (서비스 레이어에서 로그인 사용자 ID로 호출)
     */
    public void updateAudit(Long loginUserId) {
        this.modifiedBy = loginUserId;

        // 최초 생성 시 createdBy 세팅
        if (this.createdBy == null) {
            this.createdBy = loginUserId;
        }
    }

}
