package com.konglab.issue.service;

import com.konglab.issue.dto.IssueListResponseDto;
import com.konglab.issue.dto.TodayIssueResponseDto;
import com.konglab.issue.entity.IssueSnapshot;
import com.konglab.issue.repository.IssueSnapshotRepository;
import com.konglab.stock.entity.Stock;
import com.konglab.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

/**
 * 이슈 스냅샷 서비스
 *
 * 역할:
 * - 계산된 이슈 결과를 날짜별로 저장
 * - 과거 이슈 랭킹 조회 기반 마련
 */
@Service
@RequiredArgsConstructor
public class IssueSnapshotService {
    private final IssueService issueService;
    private final IssueSnapshotRepository issueSnapshotRepository;
    private final StockRepository stockRepository;

    /*
    * 특정 날짜 기준 이슈 결과를 스냅샷으로 저장
    *
    * 기존 같은 날짜 데이터는 삭제후 재저장한다.
    *
    * */

    @Transactional
    public void createSnapshot(LocalDate snapshotDate) {
        LocalDate targetDate = snapshotDate == null
                ? LocalDate.now(ZoneOffset.UTC)
                : snapshotDate;

        IssueListResponseDto issueList = issueService.getIssueList(targetDate, 0, null);

        issueSnapshotRepository.deleteBySnapshotDate(targetDate);

        List<TodayIssueResponseDto> itemList = issueList.itemList();

        for(int i = 0; i < itemList.size(); i++) {
            TodayIssueResponseDto item = itemList.get(i);

            Stock stock = stockRepository.findById(item.stockId()).orElseThrow();

            IssueSnapshot snapshot = IssueSnapshot.builder()
                    .stock(stock)
                    .snapshotDate(targetDate)
                    .rankNo(i + 1)
                    .issueScore(item.issueScore())
                    .newsCount(item.newsCount())
                    .positivePrimaryCount(item.positivePrimaryCount())
                    .negativePrimaryCount(item.negativePrimaryCount())
                    .averageRelevanceScore(item.averageRelevanceScore())
                    .timeWeightedScore(item.timeWeightedScore())
                    .positivePrimaryTitle(item.positivePrimaryTitle())
                    .positivePrimaryUrl(item.positivePrimaryUrl())
                    .negativePrimaryTitle(item.negativePrimaryTitle())
                    .negativePrimaryUrl(item.negativePrimaryUrl())
                    .build();

            issueSnapshotRepository.save(snapshot);

        }

    }
}
