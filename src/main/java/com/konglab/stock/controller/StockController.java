package com.konglab.stock.controller;

import com.konglab.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.konglab.stock.entity.Stock;
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping // GET /api/stock
    public Iterable<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

}
