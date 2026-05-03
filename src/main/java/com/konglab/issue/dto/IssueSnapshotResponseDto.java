package com.konglab.issue.dto;

import java.time.LocalDate;

/*
*
* 이슈 스냅샷 조회 응답 DTO
* */
public record IssueSnapshotResponseDto<BigDecimal>(
        Long issueSnapshotId,

        LocalDate snapshotDate,
        Integer rankNo,

        Long stockId,
        String ticker,
        String stockName,

        BigDecimal issueScore,
        Long newsCount,
        Long positivePrimaryCount,
        Long negativePrimaryCount,
        BigDecimal averageRelevanceScore,
        BigDecimal timeWeightedScore,

        String positivePrimaryTitle,
        String positivePrimaryUrl,
        String negativePrimaryTitle,
        String negativePrimaryUrl
) {
}
