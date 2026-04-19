package com.konglab.issue.controller;

import com.konglab.common.response.ApiErrorResponse;
import com.konglab.common.response.ApiResponse;
import com.konglab.issue.dto.StockIssueSummaryDto;
import com.konglab.issue.dto.TodayIssueResponseDto;
import com.konglab.issue.service.IssueService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/*
*
* 이슈 API
*
* */
@RestController
@RequestMapping("/api/issue")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    /**
     * 날짜 기준 이슈 종목 조회
     *
     * 예:
     * GET /api/issues
     * GET /api/issues?date=2026-04-19
     */
    @GetMapping
    public ApiResponse<List<TodayIssueResponseDto>> getIssueList(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer limit
    ) {
        return ApiResponse.success(issueService.getIssueList(date, limit));
    }

    /**
     * 종목 상세 이슈 요약 조회
     *
     * 예:
     * GET /api/stocks/1/issue-summary
     * GET /api/stocks/1/issue-summary?date=2026-04-19
     */

    @GetMapping("/{stockId}/issue-summary")
    public ApiResponse<StockIssueSummaryDto> getIssueSummary(
            @PathVariable Long stockId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(issueService.getIssueSummary(stockId, date));
    }


}
