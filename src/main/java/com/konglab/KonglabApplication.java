package com.konglab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 메인 애플리케이션
 *
 * @EnableJpaAuditing:
 * - @CreatedDate
 * - @LastModifiedDate
 * - @CreatedBy
 * - @LastModifiedBy
 * 동작하게 해줌
 */
@SpringBootApplication
@EnableJpaAuditing
public class KonglabApplication {

    public static void main(String[] args) {
        SpringApplication.run(KonglabApplication.class, args);
    }

}
