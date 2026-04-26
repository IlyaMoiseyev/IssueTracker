package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.IssueHistoryNotFoundException;

import com.moiseyev.issuetracker.exception.UserNotFoundException;
import com.moiseyev.issuetracker.model.dto.IssueHistoryRecordDto;
import com.moiseyev.issuetracker.model.dto.IssueHistoryResponseDto;
import com.moiseyev.issuetracker.model.dto.IssueUpdateDto;
import com.moiseyev.issuetracker.model.entity.Issue;
import com.moiseyev.issuetracker.model.entity.IssueHistory;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.model.mapper.IssueHistoryMapper;
import com.moiseyev.issuetracker.repository.IssueHistoryRepository;
import com.moiseyev.issuetracker.repository.UserRepository;
import com.moiseyev.issuetracker.security.AuthUserService;
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
  private final UserRepository userRepository;
  private final IssueHistoryMapper issueHistoryMapper;
  private final AuthUserService authUserService;

  @Autowired
  public IssueHistoryService(IssueHistoryRepository issueHistoryRepository,
                             UserRepository userRepository, IssueHistoryMapper issueHistoryMapper, AuthUserService authUserService) {
    this.issueHistoryRepository = issueHistoryRepository;
    this.userRepository = userRepository;
    this.issueHistoryMapper = issueHistoryMapper;
    this.authUserService = authUserService;
  }

  public List<IssueHistoryResponseDto> getAllHistoryRecords() {
    List<IssueHistoryResponseDto> allHistoryRecords = issueHistoryRepository
            .findAll()
            .stream()
            .map(issueHistoryMapper::toResponseDto).toList();
    if (allHistoryRecords.isEmpty()) {
      throw new IssueHistoryNotFoundException("No history records found");
    }
    return allHistoryRecords;
  }

  public IssueHistoryResponseDto getHistoryById(Long historyId) {
    IssueHistory issueHistory = issueHistoryRepository
            .findById(historyId)
            .orElseThrow(() -> new IssueHistoryNotFoundException("History with id = " + historyId + " not found"));
    return issueHistoryMapper.toResponseDto(issueHistory);
  }

  public List<IssueHistoryResponseDto> getHistoryForIssue(Long issueId) {
    List<IssueHistoryResponseDto> issueHistoryList = issueHistoryRepository
            .findByIssueIdOrderByChangedByDesc(issueId)
            .stream()
            .map(issueHistoryMapper::toResponseDto)
            .toList();
    if (issueHistoryList.isEmpty()) {
      throw new IssueHistoryNotFoundException(issueId);
    }
    return issueHistoryList;
  }

  @Transactional
  public void recordChange(Issue issue, IssueHistoryRecordDto issueHistoryRecordDto) {
    User changedBy = userRepository
            .findById(issueHistoryRecordDto.getChangedBy())
            .orElseThrow(() -> new UserNotFoundException(issueHistoryRecordDto.getChangedBy()));

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
            authUserService.getCurrentUserId()
    ));
  }

  @Transactional
  public void recordDescriptionHistory(Issue issue, IssueUpdateDto issueUpdateDto) {
    recordChange(issue, new IssueHistoryRecordDto(
            issue.getId(), "description",
            issue.getDescription(),
            issueUpdateDto.getDescription(),
            authUserService.getCurrentUserId()
    ));
  }

  @Transactional
  public void recordStatusHistory(Issue issue, IssueUpdateDto issueUpdateDto) {
    recordChange(issue, new IssueHistoryRecordDto(
            issue.getId(),
            "status",
            issue.getStatus().getName().toString(),
            issueUpdateDto.getStatusType().toUpperCase(),
            authUserService.getCurrentUserId()
    ));
  }

  @Transactional
  public void recordPriorityHistory(Issue issue, IssueUpdateDto issueUpdateDto) {
    recordChange(issue, new IssueHistoryRecordDto(
            issue.getId(),
            "priority",
            issue.getPriority().getName().toString(),
            issueUpdateDto.getPriorityType().toUpperCase(),
            authUserService.getCurrentUserId()
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
            authUserService.getCurrentUserId()
    ));
  }
}
