package com.konglab.issue.service;

import com.konglab.common.exception.BusinessException;
import com.konglab.common.exception.ErrorCode;
import com.konglab.issue.dto.*;
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
import java.util.Map;
import java.util.stream.Collectors;

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
    private IssueSnapshot snapshot;

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
    /**
     * 특정 날짜의 이슈 스냅샷 목록 조회
     */
    @Transactional(readOnly = true)
    public List<IssueSnapshotResponseDto> getSnapshotList(LocalDate snapshotDate) {

        LocalDate targetDate = snapshotDate != null
                ? snapshotDate
                : LocalDate.now(ZoneOffset.UTC);

        List<IssueSnapshot> snapshotList =
                issueSnapshotRepository.findBySnapshotDateOrderByRankNoAsc(targetDate);

        if (snapshotList.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_ISSUE_FOUND);
        }

        return snapshotList.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * IssueSnapshot 엔티티를 응답 DTO로 변환
     */
    private IssueSnapshotResponseDto toResponseDto(IssueSnapshot snapshot) {
        this.snapshot = snapshot;
        return new IssueSnapshotResponseDto(
                snapshot.getIssueSnapshotId(),
                snapshot.getSnapshotDate(),
                snapshot.getRankNo(),
                snapshot.getStock().getStockId(),
                snapshot.getStock().getTicker(),
                snapshot.getStock().getName(),
                snapshot.getIssueScore(),
                snapshot.getNewsCount(),
                snapshot.getPositivePrimaryCount(),
                snapshot.getNegativePrimaryCount(),
                snapshot.getAverageRelevanceScore(),
                snapshot.getTimeWeightedScore(),
                snapshot.getPositivePrimaryTitle(),
                snapshot.getPositivePrimaryUrl(),
                snapshot.getNegativePrimaryTitle(),
                snapshot.getNegativePrimaryUrl()
        );
    }

    /**
     * 날짜별 이슈 랭킹 비교
     */
    @Transactional(readOnly = true)
    public List<IssueSnapshotCompareResponseDto> compareSnapshot(
            LocalDate baseDate,
            LocalDate compareDate
    ) {

        LocalDate today = baseDate != null
                ? baseDate
                : LocalDate.now(ZoneOffset.UTC);

        LocalDate yesterday = compareDate != null
                ? compareDate
                : today.minusDays(1);

        List<IssueSnapshot> currentList =
                issueSnapshotRepository.findBySnapshotDateOrderByRankNoAsc(today);

        List<IssueSnapshot> previousList =
                issueSnapshotRepository.findBySnapshotDateOrderByRankNoAsc(yesterday);

        if (currentList.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_ISSUE_FOUND);
        }

        Map<Long, IssueSnapshot> previousMap =
                previousList.stream()
                        .collect(Collectors.toMap(
                                s -> s.getStock().getStockId(),
                                s -> s
                        ));

        return currentList.stream()
                .map(current -> {

                    IssueSnapshot previous =
                            previousMap.get(current.getStock().getStockId());

                    Integer previousRank =
                            previous != null ? previous.getRankNo() : null;

                    Integer rankDiff =
                            previousRank != null
                                    ? previousRank - current.getRankNo()
                                    : null;

                    RankChangeType rankChangeType = makeRankChangeType(previous, rankDiff);

                    return new IssueSnapshotCompareResponseDto(
                            current.getStock().getStockId(),
                            current.getStock().getTicker(),
                            current.getStock().getName(),
                            current.getRankNo(),
                            previousRank,
                            rankDiff,
                            rankChangeType,
                            current.getIssueScore()
                    );
                })
                .toList();
    }
    private RankChangeType makeRankChangeType(IssueSnapshot previous, Integer rankDiff) {
        // if (rankDiff == null) return null;

        return previous == null
                ? RankChangeType.NEW
                : rankDiff > 0
                  ? RankChangeType.UP
                  : rankDiff < 0
                    ? RankChangeType.DOWN
                    : RankChangeType.SAME;

    }
}
