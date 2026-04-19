package com.konglab.issue.service;

import com.konglab.common.entity.PrimaryType;
import com.konglab.common.exception.BusinessException;
import com.konglab.common.exception.ErrorCode;
import com.konglab.exchange.service.ExchangeRateService;
import com.konglab.issue.calculator.IssueScoreCalculator;
import com.konglab.issue.dto.IssueListResponseDto;
import com.konglab.issue.dto.StockIssueRawDto;
import com.konglab.issue.dto.StockIssueSummaryDto;
import com.konglab.issue.dto.TodayIssueResponseDto;
import com.konglab.stock.entity.Stock;
import com.konglab.stocknews.entity.StockNews;
import com.konglab.stocknews.repository.StockNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final StockNewsRepository stockNewsRepository;
    private final IssueScoreCalculator issueScoreCalculator;
    private final ExchangeRateService exchangeRateService;

    public IssueListResponseDto getIssueList(LocalDate date, Integer offset, Integer limit) {

        validatePaging(offset, limit);

        if (limit != null && limit < 0) {
            throw new BusinessException(ErrorCode.INVALID_INTEGER_PARAM);
        }

        LocalDate targetDate = (date != null)
                ? date
                : LocalDate.now(ZoneOffset.UTC);

        LocalDateTime baseDateTimeUtc = targetDate.atTime(23, 59, 59);

        List<StockNews> stockNewsList = stockNewsRepository.findAllActiveForIssue();

        if (stockNewsList.isEmpty()) {
            throw new BusinessException(ErrorCode.STOCK_NOT_FOUND);
        }
        List<TodayIssueResponseDto> result = stockNewsList.stream()
                .collect(Collectors.groupingBy(sn -> sn.getStock().getStockId()))
                .values()
                .stream()
                .map(list -> toRawDto(list, baseDateTimeUtc))
                .map(raw -> toResponseDto(raw, targetDate))
                .sorted(Comparator.comparing(TodayIssueResponseDto::issueScore).reversed())
                .toList();


        int totalCount = result.size();
        int safeOffset = Math.min(offset, totalCount);

        boolean hasNext;
        List<TodayIssueResponseDto> pagedItems;

        if (limit == null) {
            pagedItems = result.subList(safeOffset, totalCount);
            hasNext = false;
        } else {
            int endIndex = Math.min(safeOffset + limit, totalCount);
            pagedItems = result.subList(safeOffset, endIndex);
            hasNext = endIndex < totalCount;
        }

        return new IssueListResponseDto(
                pagedItems,
                totalCount,
                safeOffset,
                limit,
                hasNext
        );
    }

    public StockIssueSummaryDto getIssueSummary(Long stockId, LocalDate date) {
        LocalDate targetDate = (date != null)
                ? date
                : LocalDate.now(ZoneOffset.UTC);

        LocalDateTime baseDateTimeUtc = targetDate.atTime(23, 59, 59);

        List<StockNews> stockNewsList = stockNewsRepository.findByStock_StockIdOrderByNews_PublishedAtDesc(stockId);

        if (stockNewsList.isEmpty()) {
            throw new BusinessException(ErrorCode.STOCK_NOT_FOUND);
        }

        StockIssueRawDto raw = toRawDto(stockNewsList, baseDateTimeUtc);

        BigDecimal convertedPriceKrw = exchangeRateService.convertToKrw(
                raw.currentPrice(),
                raw.currency(),
                targetDate
        );

        return new StockIssueSummaryDto(
                raw.stockId(),
                raw.ticker(),
                raw.stockName(),
                raw.currentPrice(),
                raw.currency(),
                convertedPriceKrw,
                raw.newsCount(),
                raw.averageRelevanceScore(),
                raw.positivePrimaryTitle(),
                raw.positivePrimaryUrl(),
                raw.negativePrimaryTitle(),
                raw.negativePrimaryUrl(),
                issueScoreCalculator.calculate(raw)
        );

    }

    /**
     * StockNews 리스트를 이슈 계산용 DTO로 변환
     */
    private StockIssueRawDto toRawDto(List<StockNews> list, LocalDateTime baseDateTimeUtc) {
        if (list.isEmpty() || list.get(0) == null || list.get(0).getStock() == null) {
            throw new BusinessException(ErrorCode.STOCK_NOT_FOUND);
        }
        Stock stock = list.get(0).getStock();

        long newsCount = list.size();

        long positivePrimaryCount = list.stream()
                .filter(sn -> sn.getPrimaryType() == PrimaryType.P)
                .count();

        long negativePrimaryCount = list.stream()
                .filter(sn -> sn.getPrimaryType() == PrimaryType.N)
                .count();

        BigDecimal sum = list.stream()
                .map(StockNews::getRelevanceScore)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long relevanceCount = list.stream()
                .map(StockNews::getRelevanceScore)
                .filter(Objects::nonNull)
                .count();

        BigDecimal averageRelevanceScore = relevanceCount > 0
                ? sum.divide(BigDecimal.valueOf(relevanceCount), 4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        Optional<StockNews> positivePrimary = findPrimaryNews(list, PrimaryType.P);

        Optional<StockNews> negativePrimary = findPrimaryNews(list, PrimaryType.N);

        BigDecimal timeWeightedScore = list.stream()
                .map(sn -> calculateTimeWeight(sn, baseDateTimeUtc))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new StockIssueRawDto(
                stock.getStockId(),
                stock.getTicker(),
                stock.getName(),
                stock.getMarketType(),
                stock.getCurrentPrice(),
                stock.getCurrency(),
                newsCount,
                positivePrimaryCount,
                negativePrimaryCount,
                averageRelevanceScore,
                positivePrimary.map(sn -> sn.getNews().getTitle()).orElse(null),
                positivePrimary.map(sn -> sn.getNews().getUrl()).orElse(null),
                negativePrimary.map(sn -> sn.getNews().getTitle()).orElse(null),
                negativePrimary.map(sn -> sn.getNews().getUrl()).orElse(null),
                timeWeightedScore
        );
    }

    private BigDecimal calculateTimeWeight(StockNews stockNews, LocalDateTime baseDateTimeUtc) {
        LocalDateTime publishedAt = stockNews.getNews().getPublishedAt();
        String timeWeight = "0.1";
        if (publishedAt == null) {
            return BigDecimal.ZERO;
        }

        long hours = Duration.between(publishedAt, baseDateTimeUtc).toHours();
        if (hours <= 6) {
            timeWeight = "1.0";
        }

        if (hours <= 24) {
            timeWeight = "0.7";
        }

        if (hours <= 72) {
            timeWeight = "0.4";
        }
        return new BigDecimal(timeWeight);
    }

    /**
     * 집계 DTO를 응답 DTO로 변환
     */
    private TodayIssueResponseDto toResponseDto(StockIssueRawDto raw, LocalDate targetDate) {
        BigDecimal convertedPriceKrw = exchangeRateService.convertToKrw(
                raw.currentPrice(),
                raw.currency(),
                targetDate
        );

        return new TodayIssueResponseDto(
                raw.stockId(),
                raw.ticker(),
                raw.stockName(),
                raw.marketType(),
                raw.currentPrice(),
                raw.currency(),
                convertedPriceKrw,
                raw.newsCount(),
                raw.positivePrimaryCount(),
                raw.negativePrimaryCount(),
                raw.averageRelevanceScore(),
                raw.positivePrimaryTitle(),
                raw.positivePrimaryUrl(),
                raw.negativePrimaryTitle(),
                raw.negativePrimaryUrl(),
                raw.timeWeightedScore(),
                issueScoreCalculator.calculate(raw)
        );
    }
    /**
     * 대표 뉴스 선택
     *
     * 기준:
     * 1. relevanceScore 높은 순
     * 2. publishedAt 최신순
     */
    private Optional<StockNews> findPrimaryNews(List<StockNews> list, PrimaryType primaryType) {
        return list.stream()
                .filter(sn -> sn.getPrimaryType() == primaryType)
                .sorted(
                        Comparator.comparing(
                                        StockNews::getRelevanceScore,
                                        Comparator.nullsLast(Comparator.reverseOrder())
                                )
                                .thenComparing(
                                        sn -> sn.getNews().getPublishedAt(),
                                        Comparator.nullsLast(Comparator.reverseOrder())
                                )
                )
                .findFirst();
    }

    /**
     * offset / limit 기본 검증
     */
    private void validatePaging(Integer offset, Integer limit) {
        if (offset == null || offset < 0) {
            throw new BusinessException(ErrorCode.INVALID_INTEGER_PARAM);
        }

        if (limit != null && limit <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INTEGER_PARAM);
        }
    }
}
