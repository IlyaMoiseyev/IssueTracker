package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.IssueHistoryNotFoundException;

import com.moiseyev.issuetracker.model.dto.IssueHistoryRecordDto;
import com.moiseyev.issuetracker.model.dto.IssueUpdateDto;
import com.moiseyev.issuetracker.model.entity.Issue;
import com.moiseyev.issuetracker.model.entity.IssueHistory;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.repository.IssueHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class IssueHistoryService {
  private final IssueHistoryRepository issueHistoryRepository;
  private final UserService userService;

  @Autowired
  public IssueHistoryService(IssueHistoryRepository issueHistoryRepository,
                             UserService userService) {
    this.issueHistoryRepository = issueHistoryRepository;
    this.userService = userService;
  }

  public List<IssueHistory> getHistoryForIssue(Long issueId) {
    List<IssueHistory> issueHistoryList = issueHistoryRepository
            .findByIssueIdOrderByChangedByDesc(issueId);
    if (issueHistoryList.isEmpty()) {
      throw new IssueHistoryNotFoundException(issueId);
    }
    return issueHistoryList;
  }

  @Transactional
  public void recordChange(Issue issue, IssueHistoryRecordDto issueHistoryRecordDto) {
    User changedBy = userService.getUserById(issueHistoryRecordDto.getChangedBy());

    IssueHistory issueHistory = new IssueHistory();
    issueHistory.setIssue(issue);
    issueHistory.setFieldName(issueHistoryRecordDto.getFieldName());
    issueHistory.setOldValue(issueHistoryRecordDto.getOldValue());
    issueHistory.setNewValue(issueHistoryRecordDto.getNewValue());
    issueHistory.setChangedBy(changedBy);
    issueHistory.setChangedAt(Instant.now());

    log.info("Recording issue history: issueId = {}, field = {}, old value = {}, new value = {}",
            issue.getId(),
            issueHistory.getFieldName(),
            issueHistory.getOldValue(),
            issueHistory.getNewValue());
    issueHistoryRepository.save(issueHistory);
  }

  @Transactional
  public void recordTitleHistory(Issue issue, IssueUpdateDto issueUpdateDto) {
    recordChange(issue, new IssueHistoryRecordDto(
            issue.getId(),
            "title",
            issue.getTitle(),
            issueUpdateDto.getTitle(),
            issueUpdateDto.getReporterId()
    ));
  }

  @Transactional
  public void recordDescriptionHistory(Issue issue, IssueUpdateDto issueUpdateDto) {
    recordChange(issue, new IssueHistoryRecordDto(
            issue.getId(), "description",
            issue.getDescription(),
            issueUpdateDto.getDescription(),
            issueUpdateDto.getReporterId()
    ));
  }

  @Transactional
  public void recordStatusHistory(Issue issue, IssueUpdateDto issueUpdateDto) {
    recordChange(issue, new IssueHistoryRecordDto(
            issue.getId(),
            "status",
            issue.getStatus().getName().toString(),
            issueUpdateDto.getStatusType().toUpperCase(),
            issueUpdateDto.getReporterId()
    ));
  }

  @Transactional
  public void recordPriorityHistory(Issue issue, IssueUpdateDto issueUpdateDto) {
    recordChange(issue, new IssueHistoryRecordDto(
            issue.getId(),
            "priority",
            issue.getPriority().getName().toString(),
            issueUpdateDto.getPriorityType().toUpperCase(),
            issueUpdateDto.getReporterId()
    ));
  }

  @Transactional
  public void recordReporterIdHistory(Issue issue, IssueUpdateDto issueUpdateDto) {
    recordChange(issue, new IssueHistoryRecordDto(
            issue.getId(),
            "reporterId",
            issue.getReporter().getId().toString(),
            issueUpdateDto.getReporterId().toString(),
            issueUpdateDto.getReporterId()
    ));
  }

  @Transactional
  public void recordAssigneeIdHistory(Issue issue, IssueUpdateDto issueUpdateDto) {
    String oldAssigneeId = issue.getAssignee() != null
            ? issue.getAssignee().getId().toString()
            : null;
    String newAssigneeId = issueUpdateDto.getAssigneeId() != null
            ? issueUpdateDto.getAssigneeId().toString()
            : null;
    recordChange(issue, new IssueHistoryRecordDto(
            issue.getId(),
            "assigneeId",
            oldAssigneeId,
            newAssigneeId,
            issueUpdateDto.getReporterId()
    ));
  }
}
