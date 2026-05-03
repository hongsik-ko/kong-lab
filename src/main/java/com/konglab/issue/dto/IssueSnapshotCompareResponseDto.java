package com.konglab.issue.dto;

import java.math.BigDecimal;

/*
*
* 이슈 스탭샷 비교 응답 DTO
* */
public record IssueSnapshotCompareResponseDto(
        Long stockId,
        String ticker,
        String stockName,

        Integer currentRank,
        Integer previousRank,
        Integer rankDiff,

        RankChangeType rankChangeType,

        BigDecimal issueScore

) {
}
