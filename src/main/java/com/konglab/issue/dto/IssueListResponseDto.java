package com.konglab.issue.dto;

import java.util.List;

public record IssueListResponseDto (
        List<TodayIssueResponseDto> itemList,
        int totalCount,
        int offset,
        Integer limit,
        boolean hasNext
) {
}
