package com.konglab.issue.controller;

import com.konglab.common.response.ApiErrorResponse;
import com.konglab.common.response.ApiResponse;
import com.konglab.issue.dto.TodayIssueResponseDto;
import com.konglab.issue.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 
    @GetMapping("/today")
    public ApiResponse<List<TodayIssueResponseDto>> getTodayIssues() {
        return ApiResponse.success(issueService.getTodayIssues());
    }
}
