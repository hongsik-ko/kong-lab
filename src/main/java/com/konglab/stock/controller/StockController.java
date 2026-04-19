package com.konglab.stock.controller;

import com.konglab.common.response.ApiResponse;
import com.konglab.news.dto.NewsSortType;
import com.konglab.news.dto.StockNewsResponseDto;
import com.konglab.news.service.NewsService;
import com.konglab.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
    public ApiResponse<List<StockNewsResponseDto>> getStockNews(
            @PathVariable Long stockId,
            @RequestParam(defaultValue = "LATEST")String sort,
            @RequestParam(defaultValue = "false") boolean primaryOnly ) {
        NewsSortType newsSortType = NewsSortType.from(sort);
        return ApiResponse.success(newsService.getNewsByStock(stockId, newsSortType, primaryOnly));
    }

}
