package com.konglab.issue.calculator;

import com.konglab.issue.dto.StockIssueRawDto;

import java.math.BigDecimal;

/*
*
* 이슈 점수 계산기 인터페이스
*
* 점수 규칙이 바뀌더라도 서비스는 그대로 두고
* 계산기 구현체만 수정 / 교체할 수 있게 분리한다.
*
* */
public interface IssueScoreCalculator {

    /*
    * 종목별 집계 데이터를 받아 이슈 점수를 계산한다.
    * */

    BigDecimal calculate(StockIssueRawDto rawData);
}
