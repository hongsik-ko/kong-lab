package com.konglab.exchange.service;

import com.konglab.exchange.entity.ExchangeRate;
import com.konglab.exchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

/**
 * 환율 서비스
 *
 * 역할:
 * - 특정 날짜 기준 환율 조회
 * - 종목 가격을 KRW 기준으로 환산
 */

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    /**
     * KRW 환산 가격 계산
     *
     * 정책:
     * - KRW는 그대로 반환
     * - 환율 없으면 null 반환
     */
    public BigDecimal convertToKrw(BigDecimal currentPrice, String currency, LocalDate targetDate) {

        if (currentPrice == null || currency == null || targetDate == null) {
            return null;
        }

        if ("KRW".equalsIgnoreCase(currency)) {
            return currentPrice.setScale(4, RoundingMode.HALF_UP);
        }

        Optional<ExchangeRate> exchangeRateOptional =
                exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyAndRateDate(
                        currency.toUpperCase(),
                        "KRW",
                        targetDate
                );

        if (exchangeRateOptional.isEmpty()) {
            return null;
        }

        ExchangeRate exchangeRate = exchangeRateOptional.get();

        return currentPrice.multiply(exchangeRate.getExchangeRate())
                .setScale(4, RoundingMode.HALF_UP);
    }
}
