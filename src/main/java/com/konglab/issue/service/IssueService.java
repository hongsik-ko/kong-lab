package com.konglab.issue.service;

import com.konglab.common.entity.PrimaryType;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final StockNewsRepository stockNewsRepository;
    private final IssueScoreCalculator issueScoreCalculator;
    final String primeTypeP = PrimaryType.P.name();
    final String primeTypeN = PrimaryType.N.name();
    public List<TodayIssueResponseDto> getTodayIssues() {
        List<StockNews> stockNewsList = stockNewsRepository.findAllActiveForIssue();

        Map<Long, List<StockNews>> grouped = stockNewsList.stream()
                .collect(Collectors.groupingBy(sn -> sn.getStock().getStockId()));

        return grouped.values().stream()
                .map(this::toRawDto)
                .map(this::toResponseDto)
                .sorted(Comparator.comparing(TodayIssueResponseDto::issueScore).reversed())
                .toList();
    }
    public boolean checkPrimaryType(PrimaryType primaryType, String checker) {
        if (primaryType == null || checker == null) return false;
        if (checker.isEmpty()) return false;
        if (!List.of(primeTypeP, primeTypeN).contains(checker)) return false;
        return primaryType.name().equals(checker);
    }
    public StockIssueRawDto toRawDto(List<StockNews> stockNewsList) {
        Stock stock = stockNewsList.get(0).getStock();

        long newsCount = stockNewsList.size();
        long positivePrimaryCount = stockNewsList.stream()
                .filter(sn -> checkPrimaryType(sn.getPrimaryType(), primeTypeP))
                .count();
        long negativePrimaryCount = stockNewsList.stream()
                .filter(sn -> checkPrimaryType(sn.getPrimaryType(), primeTypeN))
                .count();
        BigDecimal averageRelevanceScore = stockNewsList.stream()
                .map(StockNews::getRelevanceScore)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO,BigDecimal::add); // (init, prev)
        
        long relevanceCount = stockNewsList.stream()
                .map(StockNews::getRelevanceScore)
                .filter(Objects::nonNull)
                .count();

        if (relevanceCount > 0) {
            averageRelevanceScore = averageRelevanceScore.divide(
                    BigDecimal.valueOf(relevanceCount),
                    4,
                    RoundingMode.HALF_UP
            );
        }

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
                averageRelevanceScore
        );
    }
    private TodayIssueResponseDto toResponseDto(StockIssueRawDto rawDto) {
        return new TodayIssueResponseDto(
                rawDto.stockId(),
                rawDto.ticker(),
                rawDto.stockName(),
                rawDto.marketType(),
                rawDto.currentPrice(),
                rawDto.currency(),
                rawDto.newsCount(),
                rawDto.positivePrimaryCount(),
                rawDto.negativePrimaryCount(),
                rawDto.averageRelevanceScore(),
                issueScoreCalculator.calculate(rawDto)
        );
    }
}
