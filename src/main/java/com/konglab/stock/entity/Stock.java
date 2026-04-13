package com.konglab.stock.entity;

import com.konglab.common.entity.BasicEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // JPA Entity
@Table(name = "stock") // DB Table Name
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto increment
    @Column(name = "stock_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String ticker; // 종목 코드

    @Column(nullable = false, length = 100)
    private String name; // 종목명

    @Column(nullable = false, length = 20)
    private String marketType; // KOSPI, KOSDAQ

    @Column(nullable = false)
    private String currency; // 통화


    @Column(nullable = false)
    private Long currentPrice; // 현재가

    @Column(nullable = false)
    private Double changeRate; // 등락률

}
