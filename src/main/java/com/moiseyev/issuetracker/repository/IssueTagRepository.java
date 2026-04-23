package com.moiseyev.issuetracker.repository;

import com.moiseyev.issuetracker.model.entity.IssueTag;
import com.moiseyev.issuetracker.model.enums.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueTagRepository extends JpaRepository<IssueTag, Long> {
  List<IssueTag> findByIssueId(Long issueId);

  List<IssueTag> findByTagName(TagType name);

  void deleteByIssueIdAndTagId(Long issueId, Integer tagId);

  boolean existsByIssueIdAndTagId(Long issueId, Integer tagId);
}
