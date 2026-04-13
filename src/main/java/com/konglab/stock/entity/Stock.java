package com.konglab.stock.entity;

import com.konglab.common.entity.BasicEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity // JPA Entity
@Table(name = "stock") // DB Table Name
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 보호
public class Stock extends BasicEntity {
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto increment
    @Column(name = "stock_id")
    private Long stockId;

    @Column(name = "ticker", nullable = false, unique = true, length = 20)
    private String ticker; // 종목 코드

    @Column(name = "name", nullable = false, length = 100)
    private String name; // 종목명

    @Column(name = "market_type", nullable = false, length = 20)
    private String marketType; // 시장 구분 (예: KOSPI, KOSDAQ, NASDAQ)

    @Column(name = "current_price", nullable = false, precision = 18, scale = 4)
    private BigDecimal currentPrice; // 현재가

    @Column(name = "currency", nullable = false, length = 3)
    private String currency; // 통화 코드 (예: KRW, USD)

    @Builder // builder 패턴 생성
    public Stock(String ticker, String name, String marketType, BigDecimal currentPrice, String currency) {
        this.ticker = ticker;
        this.name = name;
        this.marketType = marketType;
        this.currentPrice = currentPrice;
        this.currency = currency;
    }

    /**
     * 현재가 변경
     */
    public void changeCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    /**
     * 통화 변경
     */
    public void changeCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 시장 구분 변경
     */
    public void changeMarketType(String marketType) {
        this.marketType = marketType;
    }

    /**
     * 종목명 변경
     */
    public void changeName(String name) {
        this.name = name;
    }

}
