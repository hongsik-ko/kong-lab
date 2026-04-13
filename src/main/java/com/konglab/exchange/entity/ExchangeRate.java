package com.konglab.exchange.entity;
import com.konglab.common.entity.BasicEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 일자별 환율 정보 엔티티
 *
 * 예:
 * - USD -> KRW
 * - JPY -> KRW
 */
@Entity // JPA 엔티티
@Table(name = "exchange_rate",
        uniqueConstraints = {
                @UniqueConstraint( // 일자, 기준 통화, 대상 통화 단위로 중복 체크
                        name = "uk_exchange_rate_currency_date",
                        columnNames = {"base_currency", "target_currency", "rate_date"}
                )
        }) // DB 테이블명
@Getter // getter 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 보호
public class ExchangeRate extends BasicEntity {
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto increment
    @Column(name = "exchange_rate_id")
    private Long exchangeRateId;

    @Column(name = "base_currency", nullable = false, length = 3)
    private String baseCurrency; // 기준 통화

    @Column(name = "target_currency", nullable = false, length = 3)
    private String targetCurrency; // 대상 통화

    @Column(name = "rate_date", nullable = false)
    private LocalDate rateDate; // 환율 기준 일자

    @Column(name = "exchange_rate", nullable = false, precision = 18, scale = 6)
    private BigDecimal exchangeRate; // 환율 값

    @Builder // builder 패턴 생성
    public ExchangeRate(String baseCurrency, String targetCurrency, LocalDate rateDate, BigDecimal exchangeRate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rateDate = rateDate;
        this.exchangeRate = exchangeRate;
    }

    /**
     * 환율 값 변경
     */
    public void changeExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * 기준 통화 변경
     */
    public void changeBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    /**
     * 대상 통화 변경
     */
    public void changeTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    /**
     * 환율 기준 일자 변경
     */
    public void changeRateDate(LocalDate rateDate) {
        this.rateDate = rateDate;
    }
}
