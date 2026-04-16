package com.moiseyev.issuetracker.repository;

import com.moiseyev.issuetracker.model.entity.IssuePriority;
import com.moiseyev.issuetracker.model.enums.PriorityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssuePriorityRepository extends JpaRepository<IssuePriority, Integer> {
  Optional<IssuePriority> findByName(PriorityLevel name);
}
