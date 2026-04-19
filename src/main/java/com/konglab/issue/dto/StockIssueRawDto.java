package com.konglab.issue.dto;

import com.konglab.news.dto.StockNewsResponseDto;

import java.math.BigDecimal;

/*
*
* 이슈 점수 계산을 위한 집계 원본 DTO
*
* */
public record StockIssueRawDto(
        Long stockId, // 종목 ID
        String ticker, // 종목 코드
        String stockName, // 종목 이름
        String marketType, // 시장 유형
        BigDecimal currentPrice, // 현재가
        String currency, // 통화

        Long newsCount, // 뉴스 개수
        Long positivePrimaryCount, // 긍정 뉴스 개수
        Long negativePrimaryCount, // 부정 뉴스 개수
        BigDecimal averageRelevanceScore, // 평균 관련성 점수

        String positivePrimaryTitle, // 대표 긍정 뉴스 제목
        String positivePrimaryUrl, // 대표 긍정 뉴스 URL

        String negativePrimaryTitle, // 부정 대표 뉴스 제목
        String negativePrimaryUrl, // 부정 대표 뉴스 URL

        BigDecimal timeWeightedScore // 전체 시간 가중치

) {


}
