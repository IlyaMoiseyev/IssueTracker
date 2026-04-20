package com.moiseyev.issuetracker.repository;

import com.moiseyev.issuetracker.model.entity.IssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Long> {
  List<IssueHistory> findByIssueIdOrderByChangedByDesc(Long issueId);
}
