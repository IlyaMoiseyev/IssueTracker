package com.moiseyev.issuetracker.repository;

import com.moiseyev.issuetracker.model.entity.IssueStatus;
import com.moiseyev.issuetracker.model.enums.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueStatusRepository extends JpaRepository<IssueStatus, Integer> {
  Optional<IssueStatus> findByName(StatusType name);
}
