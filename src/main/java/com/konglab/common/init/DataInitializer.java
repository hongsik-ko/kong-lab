package com.konglab.common.init;

import com.konglab.common.entity.PrimaryType;
import com.konglab.exchange.entity.ExchangeRate;
import com.konglab.exchange.repository.ExchangeRateRepository;
import com.konglab.news.entity.News;
import com.konglab.news.repository.NewsRepository;
import com.konglab.stock.entity.Stock;
import com.konglab.stock.repository.StockRepository;
import com.konglab.stocknews.entity.StockNews;
import com.konglab.stocknews.repository.StockNewsRepository;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private final NewsRepository newsRepository;
    private final StockNewsRepository stockNewsRepository;
    /**
     * 서버 시작 후 초기 데이터 삽입
     */
    @PostConstruct // 빈 생성 후 자동 실행
    public void init() {
        initStock();
        initExchangeRate();
        initNewsAndMapping();
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
    /**
     * 뉴스 및 종목-뉴스 연결 데이터 초기화
     */
    private void initNewsAndMapping() {
        if (newsRepository.count() > 0 || stockNewsRepository.count() > 0) {
            return;
        }

        Stock samsung = stockRepository.findByTicker("005930")
                .orElseThrow(() -> new IllegalArgumentException("삼성전자 종목이 없습니다."));
        Stock skHynix = stockRepository.findByTicker("000660")
                .orElseThrow(() -> new IllegalArgumentException("SK하이닉스 종목이 없습니다."));
        Stock apple = stockRepository.findByTicker("AAPL")
                .orElseThrow(() -> new IllegalArgumentException("Apple 종목이 없습니다."));
        Stock nvidia = stockRepository.findByTicker("NVDA")
                .orElseThrow(() -> new IllegalArgumentException("NVIDIA 종목이 없습니다."));

        News semiconductorNews = newsRepository.save(
                News.builder()
                        .title("AI 반도체 수요 증가로 글로벌 반도체 업종 강세")
                        .summary("AI 서버 투자 확대와 고대역폭 메모리 수요 증가로 반도체 기업 전반에 기대감이 커졌다.")
                        .source("Kong Finance")
                        .url("https://example.com/news/semiconductor-demand")
                        .publishedAt(LocalDateTime.now().minusHours(2))
                        .sentiment("POSITIVE")
                        .build()
        );

        News techNews = newsRepository.save(
                News.builder()
                        .title("미국 기술주 강세... 대형 성장주 중심 매수세 확대")
                        .summary("미국 증시에서 기술주 중심의 강세가 이어지며 주요 빅테크 종목들이 상승 흐름을 보였다.")
                        .source("Kong Finance")
                        .url("https://example.com/news/us-tech-rally")
                        .publishedAt(LocalDateTime.now().minusHours(4))
                        .sentiment("POSITIVE")
                        .build()
        );

        News middleEastNews = newsRepository.save(
                News.builder()
                        .title("중동 지정학 리스크 확대에 원자재·물류 시장 긴장")
                        .summary("중동 지역 긴장 고조로 원자재 가격과 글로벌 물류 비용 변동성 확대 우려가 커지고 있다.")
                        .source("Kong Finance")
                        .url("https://example.com/news/middle-east-risk")
                        .publishedAt(LocalDateTime.now().minusHours(6))
                        .sentiment("NEGATIVE")
                        .build()
        );

        stockNewsRepository.save(
                StockNews.builder()
                        .stock(samsung)
                        .news(semiconductorNews)
                        .relevanceScore(new BigDecimal("0.9500"))
                        .primaryType(PrimaryType.P)
                        .build()
        );

        stockNewsRepository.save(
                StockNews.builder()
                        .stock(skHynix)
                        .news(semiconductorNews)
                        .relevanceScore(new BigDecimal("0.9800"))
                        .primaryType(PrimaryType.P)
                        .build()
        );

        stockNewsRepository.save(
                StockNews.builder()
                        .stock(nvidia)
                        .news(semiconductorNews)
                        .relevanceScore(new BigDecimal("0.8900"))
                        .primaryType(null)
                        .build()
        );

        stockNewsRepository.save(
                StockNews.builder()
                        .stock(apple)
                        .news(techNews)
                        .relevanceScore(new BigDecimal("0.9200"))
                        .primaryType(PrimaryType.P)
                        .build()
        );

        stockNewsRepository.save(
                StockNews.builder()
                        .stock(nvidia)
                        .news(techNews)
                        .relevanceScore(new BigDecimal("0.9400"))
                        .primaryType(PrimaryType.P)
                        .build()
        );

        stockNewsRepository.save(
                StockNews.builder()
                        .stock(samsung)
                        .news(middleEastNews)
                        .relevanceScore(new BigDecimal("0.4200"))
                        .primaryType(PrimaryType.N)
                        .build()
        );

        stockNewsRepository.save(
                StockNews.builder()
                        .stock(apple)
                        .news(middleEastNews)
                        .relevanceScore(new BigDecimal("0.3800"))
                        .primaryType(PrimaryType.N)
                        .build()
        );
    }
}
