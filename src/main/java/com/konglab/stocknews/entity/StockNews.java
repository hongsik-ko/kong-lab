package com.konglab.stocknews.entity;

import com.konglab.common.entity.BasicEntity;
import com.konglab.common.entity.PrimaryType;
import com.konglab.news.entity.News;
import com.konglab.stock.entity.Stock;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 종목 - 뉴스 연결 엔티티
 *
 * 하나의 뉴스가 여러 종목에 연결될 수 있고,
 * 하나의 종목도 여러 뉴스를 가질 수 있으므로
 * N:M 관계를 중간 엔티티로 푼다.
 */
@Entity // JPA 엔티티
@Table(
        name = "stock_news",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_stock_news_stock_id_news_id",
                        columnNames = {"stock_id", "news_id"}
                )
        }
)
@Getter // getter 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 보호
public class StockNews extends BasicEntity {
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto increment
    @Column(name = "stock_news_id")
    private Long stockNewsId;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 연결이 하나의 종목을 참조
    @JoinColumn(name = "stock_id", nullable = false) // FK
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 연결이 하나의 뉴스를 참조
    @JoinColumn(name = "news_id", nullable = false) // FK
    private News news;

    @Column(name = "relevance_score", precision = 5, scale = 4)
    private BigDecimal relevanceScore; // 관련도 점수 (0.0000 ~ 9.9999 등 정책은 추후)

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_type", length = 1)
    /*
    *
    * 해당건은 종목별 P,N은 최대 한개의 값을 가진다. (두개 이상일 경우 에러로 간주)
    * */
    private PrimaryType primaryType; // 대표 뉴스 여부

    @Builder // builder 패턴 생성
    public StockNews(
            Stock stock,
            News news,
            BigDecimal relevanceScore,
            PrimaryType primaryType
    ) {
        this.stock = stock;
        this.news = news;
        this.relevanceScore = relevanceScore;
        this.primaryType = primaryType;
    }

    /**
     * 관련도 점수 변경
     */
    public void changeRelevanceScore(BigDecimal relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    /**
     * 대표 뉴스 여부 변경
     */
    public void changePrimary(PrimaryType primaryType) {
        this.primaryType = primaryType;
    }
}
