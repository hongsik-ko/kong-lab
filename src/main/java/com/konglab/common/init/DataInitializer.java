package com.konglab.common.init;

import com.konglab.exchange.entity.ExchangeRate;
import com.konglab.exchange.repository.ExchangeRateRepository;
import com.konglab.stock.entity.Stock;
import com.konglab.stock.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 서버 실행 시 샘플 데이터를 초기화하는 클래스
 *
 * 현재는 MVP 확인용 샘플 데이터를 넣는다.
 * 나중에 외부 API 연동 시 이 코드는 제거하거나 프로파일 분리 가능하다.
 */
@Component // 스프링 빈 등록
@RequiredArgsConstructor // final 필드 생성자 주입
public class DataInitializer {
    private final StockRepository stockRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    /**
     * 서버 시작 후 초기 데이터 삽입
     */
    @PostConstruct // 빈 생성 후 자동 실행
    public void init() {
        initStock();
        initExchangeRate();
    }

    /**
     * 종목 샘플 데이터 초기화
     */
    private void initStock() {
        if (stockRepository.count() > 0) {
            return; // 이미 데이터가 있으면 스킵
        }

        stockRepository.save(
                Stock.builder()
                        .ticker("005930")
                        .name("삼성전자")
                        .marketType("KOSPI")
                        .currentPrice(new BigDecimal("85000.0000"))
                        .currency("KRW")
                        .build()
        );

        stockRepository.save(
                Stock.builder()
                        .ticker("000660")
                        .name("SK하이닉스")
                        .marketType("KOSPI")
                        .currentPrice(new BigDecimal("120000.0000"))
                        .currency("KRW")
                        .build()
        );

        stockRepository.save(
                Stock.builder()
                        .ticker("AAPL")
                        .name("Apple")
                        .marketType("NASDAQ")
                        .currentPrice(new BigDecimal("182.3500"))
                        .currency("USD")
                        .build()
        );

        stockRepository.save(
                Stock.builder()
                        .ticker("NVDA")
                        .name("NVIDIA")
                        .marketType("NASDAQ")
                        .currentPrice(new BigDecimal("903.1500"))
                        .currency("USD")
                        .build()
        );
    }

    /**
     * 환율 샘플 데이터 초기화
     */
    private void initExchangeRate() {
        if (exchangeRateRepository.count() > 0) {
            return; // 이미 데이터가 있으면 스킵
        }

        exchangeRateRepository.save(
                ExchangeRate.builder()
                        .baseCurrency("USD")
                        .targetCurrency("KRW")
                        .rateDate(LocalDate.now())
                        .exchangeRate(new BigDecimal("1385.200000"))
                        .build()
        );

        exchangeRateRepository.save(
                ExchangeRate.builder()
                        .baseCurrency("JPY")
                        .targetCurrency("KRW")
                        .rateDate(LocalDate.now())
                        .exchangeRate(new BigDecimal("9.120000"))
                        .build()
        );
    }
}
