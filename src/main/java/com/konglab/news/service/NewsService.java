package com.konglab.news.service;

import com.konglab.common.entity.PrimaryType;
import com.konglab.common.exception.BusinessException;
import com.konglab.common.exception.ErrorCode;
import com.konglab.issue.service.IssueCacheService;
import com.konglab.news.dto.NewsSortType;
import com.konglab.news.dto.StockNewsResponseDto;
import com.konglab.news.entity.News;
import com.konglab.news.repository.NewsRepository;
import com.konglab.stocknews.entity.StockNews;
import com.konglab.stocknews.repository.StockNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final StockNewsRepository stockNewsRepository;
    private final IssueCacheService issueCacheService;;
    private final NewsRepository newsRepository;

    /**
     * 종목별 뉴스 조회
     */
    public List<StockNewsResponseDto> getNewsByStock(Long stockId, NewsSortType sort, boolean primaryOnly) {

        List<StockNews> stockNewsList =
                stockNewsRepository.findByStock_StockIdOrderByNews_PublishedAtDesc(stockId);
        if (stockNewsList == null || stockNewsList.isEmpty()) {
            throw new BusinessException(ErrorCode.STOCK_NOT_FOUND);
        }

        // 1. primaryOnly filter
        if (primaryOnly) {
            stockNewsList = stockNewsList.stream()
                    .filter(stockNews -> stockNews.getPrimaryType() != null)
                    .toList();
        }

        // 2. sort
        applySorting(stockNewsList, sort);
        

        return stockNewsList.stream()
                .map(StockNewsResponseDto::from)
                .toList();
    }

    public void saveNews(News news) {
        newsRepository.save(news);
        issueCacheService.evitAll();
    }

    private List<StockNews> applySorting(List<StockNews> list, NewsSortType sort) {

        return NewsSortType.LATEST.name().equals(sort.name())
                ? list
                : list.stream()
                  .sorted(Comparator.comparing(
                          StockNews::getRelevanceScore,
                          Comparator.nullsLast(Comparator.reverseOrder())
                  ))
                  .toList();

    }
}
