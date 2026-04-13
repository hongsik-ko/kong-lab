package com.konglab.stock.repository;

import com.konglab.stock.entity.Stock;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


/*
* 종목 조회 Repository
* */
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByTicker(String ticker);
}
