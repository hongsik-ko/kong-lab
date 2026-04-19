package com.konglab.issue.dto;

import java.math.BigDecimal;


/*
*
* 종목 상세 이슈 요약 DTO
* */
public record StockIssueSummaryDto (
        Long stockId, // 종목 ID
        String ticker, // 종목 코드
        String stockName, // 종목 이름
        BigDecimal currentPrice, // 현재가
        String currency, // 통화
        BigDecimal convertedPriceKrw, // 현재가(KRW)

        Long newsCount, // 뉴스 개수
        BigDecimal averageRelevanceScore, // 퍙균 관련성 점수

        String positivePrimaryTitle, // 긍정 대표 뉴스 제목
        String positivePrimaryUrl, // 긍정 대표 뉴스 URL

        String negativePrimaryTitle, // 부정 대표 뉴스 제목
        String negativePrimaryUrl, // 부정 대표 뉴스 URL

        BigDecimal issueScore // 이슈 점수
) {
}
