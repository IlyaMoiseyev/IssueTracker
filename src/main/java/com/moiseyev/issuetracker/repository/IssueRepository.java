package com.moiseyev.issuetracker.repository;

import com.moiseyev.issuetracker.model.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
  List<Issue> findByReporterId(Long reporterId);

  List<Issue> findByAssigneeId(Long assigneeId);
}
