package com.konglab.stock.controller;

import com.konglab.common.response.ApiResponse;
import com.konglab.news.dto.StockNewsResponseDto;
import com.konglab.news.service.NewsService;
import com.konglab.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.konglab.stock.entity.Stock;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    private final NewsService newsService;

    @GetMapping
    public ApiResponse<List<Stock>> getAllStocks() {
        return ApiResponse.success(stockService.getAllStocks());
    }

    @GetMapping("/{stockId}/news")
    public ApiResponse<List<StockNewsResponseDto>> getStockNews(@PathVariable Long stockId) {
        return ApiResponse.success(newsService.getNewsByStock(stockId));
    }

}
