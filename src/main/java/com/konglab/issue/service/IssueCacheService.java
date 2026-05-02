package com.konglab.issue.service;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * 이슈 관련 캐시 관리 서비스
 */
@Service
public class IssueCacheService {

    /**
     * 이슈 리스트 캐시 전체 삭제
     */
    @CacheEvict(value = "issueList", allEntries = true)
    public void evictIssueListCache() {
        // @CacheEvict 동작용 메서드
    }

    /**
     * 이슈 상세 캐시 전체 삭제
     */
    @CacheEvict(value = "issueSummary", allEntries = true)
    public void evictIssueSummaryCache() {
        // @CacheEvict 동작용 메서드
    }

    /**
     * 이슈 관련 캐시 전체 삭제
     */
    @CacheEvict(value = {"issueList", "issueSummary"}, allEntries = true)
    public void evictAllIssueCache() {
        // @CacheEvict 동작용 메서드
    }
}
