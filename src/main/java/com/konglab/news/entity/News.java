package com.konglab.news.entity;
import com.konglab.common.entity.BasicEntity;
import com.konglab.stock.entity.Stock;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "news")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News {
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto increment
    @Column(name = "news_id")
    private Long newsId;

    @Column(name = "title", nullable = false, length = 300)
    private String title; // 뉴스 제목

    @Column(name = "summary", length = 2000)
    private String summary; // 뉴스 요약

    @Column(name = "source", nullable = false, length = 100)
    private String source; // 출처

    @Column(name = "url", nullable = false, length = 500, unique = true)
    private String url; // 원문 링크

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt; // 기사 발행 시각

    @Column(name = "sentiment", length = 20)
    private String sentiment; // 감성값

    @Builder // builder 패턴 생성
    public News(
            String title,
            String summary,
            String source,
            String url,
            LocalDateTime publishedAt,
            String sentiment
    ) {
        this.title = title;
        this.summary = summary;
        this.source = source;
        this.url = url;
        this.publishedAt = publishedAt;
        this.sentiment = sentiment;
    }

    /**
     * 뉴스 제목 변경
     */
    public void changeTitle(String title) {
        this.title = title;
    }

    /**
     * 뉴스 요약 변경
     */
    public void changeSummary(String summary) {
        this.summary = summary;
    }

    /**
     * 감성값 변경
     */
    public void changeSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}
