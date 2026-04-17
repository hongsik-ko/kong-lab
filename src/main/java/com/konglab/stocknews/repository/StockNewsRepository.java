package com.konglab.stocknews.repository;

import com.konglab.issue.dto.StockIssueRawDto;
import com.konglab.stocknews.entity.StockNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
*
* 종목 - 뉴스 연결 Repository
* */
public interface StockNewsRepository extends JpaRepository<StockNews, Long> {

    /*
    * 종목 ID로 연결된 뉴스 목록 조회
    * 최신 뉴스 순으로 보려면 서비스/쿼리 확장 가능
    * */
    List<StockNews> findByStock_StockId(Long stockId);

    /*
    *
    * 뉴스 ID로 연결된 종목 목록 조회
    * */
    List<StockNews> findByNews_NewsId(Long newsId);
    /*
    * 종목별 연결 개수 조회
    * */
    long countByStock_StockId(Long stockId);

    /*
    *
    * 종목 기준 뉴스 조회 (뉴스의 최신순 정렬)
    * */
    List<StockNews> findByStock_StockIdOrderByNews_PublishedAtDesc(Long stockId);
    /*
    *
    * 종목별 이슈 점수 계산용 집계 조회
    * */
    @Query("""
            select sn
            from StockNews sn 
            join fetch sn.stock s
            join fetch sn.news n
            where s.recordStatus = com.konglab.common.entity.RecordStatus.N
                and n.recordStatus = com.konglab.common.entity.RecordStatus.N
                and sn.recordStatus = com.konglab.common.entity.RecordStatus.N         
            """)
    List<StockNews> findAllActiveForIssue();
}
