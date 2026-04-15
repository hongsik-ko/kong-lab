package com.konglab.news.repository;

import com.konglab.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 뉴스 Repository
 */

public interface NewsRepository extends JpaRepository<News, Long> {
    /*
    * 뉴스 URL로 조회
    * 같은 뉴스 중복 저장 방지용
    *
    * */
    Optional<News> findByUrl(String url);
}
