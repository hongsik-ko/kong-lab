package com.konglab.issue.dto;

import com.konglab.news.dto.StockNewsResponseDto;

import java.math.BigDecimal;

/*
*
* 이슈 점수 계산을 위한 집계 원본 DTO
*
* */
public record StockIssueRawDto(
        Long stockId,
        String ticker,
        String stockName,
        String marketType,
        BigDecimal currentPrice,
        String currency,

        Long newsCount,
        Long positivePrimaryCount,
        Long negativePrimaryCount,
        BigDecimal averageRelevanceScore

) {


}
