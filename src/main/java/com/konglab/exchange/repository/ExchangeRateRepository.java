package com.konglab.exchange.repository;
import com.konglab.exchange.entity.ExchangeRate;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 환율 Repository
 */

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long>{
    /**
     * 기준 통화 + 대상 통화 + 기준 일자로 환율 조회
     */
    Optional<ExchangeRate> findByBaseCurrencyAndTargetCurrencyAndRateDate(
            String baseCurrency,
            String targetCurrency,
            LocalDate rateDate
    );
}
