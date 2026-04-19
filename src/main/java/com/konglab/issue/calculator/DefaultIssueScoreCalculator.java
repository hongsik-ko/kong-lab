package com.konglab.issue.calculator;

import com.konglab.issue.dto.StockIssueRawDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;


/*
*
* 기본 이슈 점수 계산기
* 현재 규칙 :
*   - 뉴스 개수 * 1.5
*   - 긍정 대표 뉴스 존재시 + 2
*   - 부정 대표 뉴스 존재시 + 2
*   - 평균 관련도 * 5
*
* */
@Component
public class DefaultIssueScoreCalculator implements IssueScoreCalculator{
    @Override
    public BigDecimal calculate(StockIssueRawDto rawData) {
        BigDecimal newsScore = BigDecimal.valueOf(rawData.newsCount()).multiply(new BigDecimal("1.5"));

        BigDecimal positiveBonus = rawData.positivePrimaryCount() > 0
                ? new BigDecimal("2.0")
                : BigDecimal.ZERO;

        BigDecimal negativeBonus = rawData.negativePrimaryCount() > 0
                ? new BigDecimal("2.0")
                : BigDecimal.ZERO;

        BigDecimal relevanceScore = rawData.averageRelevanceScore().multiply(new BigDecimal("5.0"));

        BigDecimal timeScore = rawData.timeWeightedScore()
                .multiply(new BigDecimal("2.0"));

        return newsScore
                .add(positiveBonus)
                .add(negativeBonus)
                .add(relevanceScore)
                .add(timeScore)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
