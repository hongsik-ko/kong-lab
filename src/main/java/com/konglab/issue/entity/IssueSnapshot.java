package com.konglab.issue.entity;


import com.konglab.common.entity.BasicEntity;
import com.konglab.stock.entity.Stock;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
* 날짜별 종목 이슈 계산 결과 스냅샷
*
* 역할 :
*   - 특정 날짜의 이슈 점수/랭킹 결과 저장
*   - 과거 이슈 히스토리 조회 기반
* */
@Entity
@Table(
        name = "issue_snapshot",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_issue_snapshot_stock_date", columnNames = {"stock_id", "snapshot_date"})
        }
)
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class IssueSnapshot extends BasicEntity {
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto increment
    @Column(name = "issue_snapshot_id")
    private Long issueSnapshotId;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 스냅샷이 하나의 종목 참조
    @JoinColumn(name = "stock_id", nullable = false) // FK
    private Stock stock;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate; // 스냅샷 기준 날짜

    @Column(name = "rank_no", nullable = false)
    private Integer rankNo; // 해당 날짜 이슈 순위

    @Column(name = "issue_score", nullable = false, precision = 18, scale = 4)
    private BigDecimal issueScore; // 이슈 점수

    @Column(name = "news_count", nullable = false)
    private Long newsCount; // 연결 뉴스 개수

    @Column(name = "positive_primary_count", nullable = false)
    private Long positivePrimaryCount; // 긍정 대표 뉴스 개수

    @Column(name = "negative_primary_count", nullable = false)
    private Long negativePrimaryCount; // 부정 대표 뉴스 개수

    @Column(name = "average_relevance_score", nullable = false, precision = 18, scale = 4)
    private BigDecimal averageRelevanceScore; // 평균 관련도

    @Column(name = "time_weighted_score", nullable = false, precision = 18, scale = 4)
    private BigDecimal timeWeightedScore; // 시간 가중 점수

    @Column(name = "positive_primary_title", length = 300)
    private String positivePrimaryTitle; // 긍정 대표 뉴스 제목

    @Column(name = "positive_primary_url", length = 500)
    private String positivePrimaryUrl; // 긍정 대표 뉴스 URL

    @Column(name = "negative_primary_title", length = 300)
    private String negativePrimaryTitle; // 부정 대표 뉴스 제목

    @Column(name = "negative_primary_url", length = 500)
    private String negativePrimaryUrl; // 부정 대표 뉴스 URL

    @Builder // builder 패턴 생성
    public IssueSnapshot(
            Stock stock,
            LocalDate snapshotDate,
            Integer rankNo,
            BigDecimal issueScore,
            Long newsCount,
            Long positivePrimaryCount,
            Long negativePrimaryCount,
            BigDecimal averageRelevanceScore,
            BigDecimal timeWeightedScore,
            String positivePrimaryTitle,
            String positivePrimaryUrl,
            String negativePrimaryTitle,
            String negativePrimaryUrl
    ) {
        this.stock = stock;
        this.snapshotDate = snapshotDate;
        this.rankNo = rankNo;
        this.issueScore = issueScore;
        this.newsCount = newsCount;
        this.positivePrimaryCount = positivePrimaryCount;
        this.negativePrimaryCount = negativePrimaryCount;
        this.averageRelevanceScore = averageRelevanceScore;
        this.timeWeightedScore = timeWeightedScore;
        this.positivePrimaryTitle = positivePrimaryTitle;
        this.positivePrimaryUrl = positivePrimaryUrl;
        this.negativePrimaryTitle = negativePrimaryTitle;
        this.negativePrimaryUrl = negativePrimaryUrl;
    }
}
