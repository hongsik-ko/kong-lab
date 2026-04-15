package com.konglab.news.dto;

import com.konglab.stocknews.entity.StockNews;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
*
* 종복별 뉴스 응답 DTO
* */
public record StockNewsResponseDto (
        Long newsId,
        String title,
        String summary,
        String source,
        String url,
        LocalDateTime publishedAt,
        String sentiment,
        BigDecimal relevanceScore,
        String primaryType) {
    public static StockNewsResponseDto from(StockNews stockNews) {
        return new StockNewsResponseDto(
                stockNews.getNews().getNewsId(),
                stockNews.getNews().getTitle(),
                stockNews.getNews().getSummary(),
                stockNews.getNews().getSource(),
                stockNews.getNews().getUrl(),
                stockNews.getNews().getPublishedAt(),
                stockNews.getNews().getSentiment(),
                stockNews.getRelevanceScore(),
                stockNews.getPrimaryType() != null ? stockNews.getPrimaryType().name() : null
        );
    }
}
