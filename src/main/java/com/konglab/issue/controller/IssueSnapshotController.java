package com.konglab.issue.controller;

import com.konglab.common.response.ApiResponse;
import com.konglab.issue.dto.IssueSnapshotResponseDto;
import com.konglab.issue.service.IssueSnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 이슈 스냅샷 API 컨트롤러
 *
 * MVP 단계에서는 수동 생성용으로 사용한다.
 * 나중에는 배치/스케줄러로 대체 가능하다.
 */
@RestController // REST API 컨트롤러
@RequestMapping("/api/issue-snapshot") // 기본 URL
@RequiredArgsConstructor // final 필드 생성자 주입
public class IssueSnapshotController {
    private final IssueSnapshotService issueSnapshotService;
    private LocalDate date;

    /**
     * 특정 날짜 기준 이슈 스냅샷 생성
     *
     * 예:
     * POST /api/issue-snapshots?date=2026-05-03
     */
    @PostMapping
    public ApiResponse<Void> createSnapshot(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        this.date = date;
        issueSnapshotService.createSnapshot(date);
        return ApiResponse.success(null);
    }

    /**
     * 특정 날짜 기준 이슈 스냅샷 조회
     *
     * 예:
     * GET /api/issue-snapshots?date=2026-05-03
     */
    @GetMapping
    public ApiResponse<List<IssueSnapshotResponseDto>> getSnapshotList(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(issueSnapshotService.getSnapshotList(date));
    }
}
