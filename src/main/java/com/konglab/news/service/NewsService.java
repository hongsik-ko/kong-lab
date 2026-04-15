package com.konglab.news.service;

import com.konglab.news.dto.StockNewsResponseDto;
import com.konglab.stocknews.entity.StockNews;
import com.konglab.stocknews.repository.StockNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final StockNewsRepository stockNewsRepository;

    /**
     * 종목별 뉴스 조회
     */
    public List<StockNewsResponseDto> getNewsByStock(Long stockId) {

        List<StockNews> stockNewsList =
                stockNewsRepository.findByStock_StockIdOrderByNews_PublishedAtDesc(stockId);

        return stockNewsList.stream()
                .map(StockNewsResponseDto::from)
                .collect(Collectors.toList());
    }
}
