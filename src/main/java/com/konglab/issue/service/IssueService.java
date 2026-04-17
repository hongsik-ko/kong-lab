package com.konglab.issue.service;

import com.konglab.common.entity.PrimaryType;
import com.konglab.common.exception.BusinessException;
import com.konglab.common.exception.ErrorCode;
import com.konglab.issue.calculator.IssueScoreCalculator;
import com.konglab.issue.dto.StockIssueRawDto;
import com.konglab.issue.dto.TodayIssueResponseDto;
import com.konglab.stock.entity.Stock;
import com.konglab.stocknews.entity.StockNews;
import com.konglab.stocknews.repository.StockNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final StockNewsRepository stockNewsRepository;
    private final IssueScoreCalculator issueScoreCalculator;

    public List<TodayIssueResponseDto> getTodayIssues() {
        List<StockNews> stockNewsList = stockNewsRepository.findAllActiveForIssue();

        if (stockNewsList.isEmpty()) {
            throw new BusinessException(ErrorCode.STOCK_NOT_FOUND);
        }

        Map<Long, List<StockNews>> grouped = stockNewsList.stream()
                .collect(Collectors.groupingBy(sn -> sn.getStock().getStockId()));

        return grouped.values().stream()
                .map(this::toRawDto)
                .map(this::toResponseDto)
                .sorted(Comparator.comparing(TodayIssueResponseDto::issueScore).reversed())
                .toList();
    }
    /**
     * StockNews 리스트를 이슈 계산용 DTO로 변환
     */
    private StockIssueRawDto toRawDto(List<StockNews> list) {
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

        Optional<StockNews> positivePrimary = list.stream()
                .filter(sn -> sn.getPrimaryType() == PrimaryType.P)
                .findFirst();

        Optional<StockNews> negativePrimary = list.stream()
                .filter(sn -> sn.getPrimaryType() == PrimaryType.N)
                .findFirst();

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
                negativePrimary.map(sn -> sn.getNews().getUrl()).orElse(null)
        );
    }

    /**
     * 집계 DTO를 응답 DTO로 변환
     */
    private TodayIssueResponseDto toResponseDto(StockIssueRawDto raw) {
        return new TodayIssueResponseDto(
                raw.stockId(),
                raw.ticker(),
                raw.stockName(),
                raw.marketType(),
                raw.currentPrice(),
                raw.currency(),
                raw.newsCount(),
                raw.positivePrimaryCount(),
                raw.negativePrimaryCount(),
                raw.averageRelevanceScore(),
                raw.positivePrimaryTitle(),
                raw.positivePrimaryUrl(),
                raw.negativePrimaryTitle(),
                raw.negativePrimaryUrl(),
                issueScoreCalculator.calculate(raw)
        );
    }
}
