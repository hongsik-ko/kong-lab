package com.konglab.issue.dto;

import java.math.BigDecimal;

/*
*
* 오늘의 이슈 종목 응답 Dto
*
* */
public record TodayIssueResponseDto(
        Long stockId, // 종목 ID
        String ticker, // 종목코드
        String stockName, // 종목 명
        String marketType, // 시장 유형
        BigDecimal currentPrice, // 현재가
        String currency, // 통화
        BigDecimal convertedPriceKrw, // 현재가(KRW)

        Long newsCount, // 뉴스 개수
        Long positivePrimaryCount, // 긍정 뉴스 개수
        Long negativePrimaryCount, // 부정 뉴스 개수
        BigDecimal averageRelevanceScore, // 평균 관련성 점수

        String positivePrimaryTitle, // 긍정 대표 뉴스 이름
        String positivePrimaryUrl, // 긍정 대표 뉴스 URL

        String negativePrimaryTitle, // 부정 대표 뉴스 이름
        String negativePrimaryUrl, // 부정 대표 뉴스 URL

        BigDecimal timeWeightedScore, // 시간 가중치
        BigDecimal issueScore // 이슈 점수
) {
}
