package com.konglab.issue.dto;

import java.math.BigDecimal;

/*
*
* 오늘의 이슈 종목 응답 Dto
*
* */
public record TodayIssueResponseDto(
        Long stockId,
        String ticker,
        String stockName,
        String marketType,
        BigDecimal currentPrice,
        String currency,

        Long newsCount,
        Long positivePrimaryCount,
        Long negativePrimaryCount,
        BigDecimal averageRelevanceScore,

        String positivePrimaryTitle,
        String positivePrimaryUrl,

        String negativePrimaryTitle,
        String negativePrimaryUrl,

        BigDecimal issueScore
) {
}
