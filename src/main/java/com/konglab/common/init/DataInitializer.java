package com.konglab.common.init;

import com.konglab.common.context.SessionUserContext;
import com.konglab.common.context.SessionUserProvider;
import com.konglab.stock.entity.Stock;
import com.konglab.stock.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final StockRepository stockRepository;
    private final SessionUserProvider sessionUserProvider;
    @PostConstruct // 서버 시작시 실행
    public void init () {
        sessionUserProvider.getCurrentUser();
        if (stockRepository.count() == 0) {
            // Stock에 최초 데이터 생성
            makeStockData();
        }

    }

    private void makeStockData() {
        Stock stockData1 = Stock.builder()
                .ticker("005930")
                .name("삼성전자")
                .marketType("KOSPI")
                .currentPrice(85000L)
                .changeRate(2.3)
                .currency("KRW")
                .build();
        stockData1.setInitData();

        Stock stockData2 = Stock.builder()
                .ticker("000660")
                .name("SK하이닉스")
                .marketType("KOSPI")
                .currentPrice(120000L)
                .changeRate(1.8)
                .currency("KRW")
                .build();
        stockData2.setInitData();

        stockRepository.save(stockData1);
        stockRepository.save(stockData2);
    }
}
