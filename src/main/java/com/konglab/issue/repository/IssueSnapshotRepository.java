package com.konglab.issue.repository;

import com.konglab.issue.entity.IssueSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/*
* 이슈 스냅샷 Repository
* */
public interface IssueSnapshotRepository extends JpaRepository<IssueSnapshot, Long> {
    /**
     * 특정 날짜의 이슈 스냅샷 목록 조회
     */
    List<IssueSnapshot> findBySnapshotDateOrderByRankNoAsc(LocalDate snapshotDate);

    /**
     * 특정 날짜 기존 스냅샷 삭제
     *
     * 같은 날짜 기준으로 재계산 저장할 때 사용
     */
    void deleteBySnapshotDate(LocalDate snapshotDate);
}
