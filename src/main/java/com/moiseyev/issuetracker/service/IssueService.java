package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.EmptyIssueListException;
import com.moiseyev.issuetracker.exception.IssueNotFoundException;
import com.moiseyev.issuetracker.exception.IssueUpdateException;
import com.moiseyev.issuetracker.exception.UserNotFoundException;
import com.moiseyev.issuetracker.model.dto.IssueCreateDto;
import com.moiseyev.issuetracker.model.dto.IssueResponseDto;
import com.moiseyev.issuetracker.model.dto.IssueUpdateDto;
import com.moiseyev.issuetracker.model.entity.Issue;
import com.moiseyev.issuetracker.model.entity.IssuePriority;
import com.moiseyev.issuetracker.model.entity.IssueStatus;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.model.mapper.IssueMapper;
import com.moiseyev.issuetracker.repository.IssueRepository;
import com.moiseyev.issuetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class IssueService {
  private final IssueRepository issueRepository;
  private final IssueStatusService issueStatusService;
  private final IssuePriorityService issuePriorityService;
  private final UserRepository userRepository;
  private final IssueHistoryService issueHistoryService;
  private final IssueMapper issueMapper;

  @Autowired
  public IssueService(IssueRepository issueRepository,
                      IssueStatusService issueStatusService,
                      IssuePriorityService issuePriorityService,
                      UserRepository userRepository,
                      IssueHistoryService issueHistoryService, IssueMapper issueMapper) {
    this.issueRepository = issueRepository;
    this.issueStatusService = issueStatusService;
    this.issuePriorityService = issuePriorityService;
    this.userRepository = userRepository;
    this.issueHistoryService = issueHistoryService;
    this.issueMapper = issueMapper;
  }

  public List<IssueResponseDto> getAllIssues() {
    List<IssueResponseDto> issues = issueRepository
            .findAll()
            .stream()
            .map(issueMapper::toResponseDto)
            .toList();
    if (issues.isEmpty()) throw new EmptyIssueListException(issues.size());
    return issues;
  }

  public IssueResponseDto getIssueById(Long id) {
    Issue issue = issueRepository.findById(id).orElseThrow(() -> new IssueNotFoundException(id));
    return issueMapper.toResponseDto(issue);
  }

  @Transactional
  public IssueResponseDto createIssue(IssueCreateDto issueCreateDto) {
    IssueStatus status = issueStatusService.getIssueStatusByName(issueCreateDto.getStatus());
    IssuePriority priority = issuePriorityService.getIssuePriorityByName(issueCreateDto.getPriority());
    User reporter = userRepository
            .findById(issueCreateDto.getReporterId())
            .orElseThrow(() -> new UserNotFoundException(issueCreateDto.getReporterId()));
    User assignee = issueCreateDto.getAssigneeId() != null
            ? userRepository.findById(issueCreateDto.getAssigneeId())
            .orElseThrow(() -> new UserNotFoundException(issueCreateDto.getAssigneeId()))
            : null;

    Issue issue = new Issue();
    issue.setTitle(issueCreateDto.getTitle());
    issue.setDescription(issueCreateDto.getDescription());
    issue.setStatus(status);
    issue.setPriority(priority);
    issue.setReporter(reporter);
    issue.setAssignee(assignee);
    issue.setCreatedAt(Instant.now());
    issue.setUpdatedAt(Instant.now());

    return issueMapper.toResponseDto(issueRepository.save(issue));
  }

  @Transactional
  public IssueResponseDto updateIssue(IssueUpdateDto issueUpdateDto) {
    Issue issue = issueRepository
            .findById(issueUpdateDto.getId())
            .orElseThrow(() -> new IssueNotFoundException(issueUpdateDto.getId()));
    boolean hasChanges = false;

    if ((issueUpdateDto.getTitle() != null && !issueUpdateDto.getTitle().isBlank())
            && (!issue.getTitle().equals(issueUpdateDto.getTitle()))) {
      issueHistoryService.recordTitleHistory(issue, issueUpdateDto);
      issue.setTitle(issueUpdateDto.getTitle());
      hasChanges = true;
    }
    if ((issueUpdateDto.getDescription() != null && !issueUpdateDto.getDescription().isBlank()) &&
            (!issue.getDescription().equals(issueUpdateDto.getDescription()))) {
      issueHistoryService.recordDescriptionHistory(issue, issueUpdateDto);
      issue.setDescription(issueUpdateDto.getDescription());
      hasChanges = true;
    }
    if ((issueUpdateDto.getStatusType() != null && !issueUpdateDto.getStatusType().isBlank()) &&
            (!issue.getStatus().equals(issueStatusService.getIssueStatusByName(issueUpdateDto.getStatusType())))) {
      issueHistoryService.recordStatusHistory(issue, issueUpdateDto);
      issue.setStatus(issueStatusService.getIssueStatusByName(issueUpdateDto.getStatusType()));
      hasChanges = true;
    }
    if ((issueUpdateDto.getPriorityType() != null && !issueUpdateDto.getPriorityType().isBlank()) &&
            (!issue.getPriority().equals(issuePriorityService.getIssuePriorityByName(issueUpdateDto.getPriorityType())))) {
      issueHistoryService.recordPriorityHistory(issue, issueUpdateDto);
      issue.setPriority(issuePriorityService.getIssuePriorityByName(issueUpdateDto.getPriorityType()));
      hasChanges = true;
    }
    if (issueUpdateDto.getReporterId() != null && !issue.getReporter().getId().equals(issueUpdateDto.getReporterId())) {
      issueHistoryService.recordReporterIdHistory(issue, issueUpdateDto);
      User reporter = userRepository.findById(issueUpdateDto.getReporterId())
              .orElseThrow(() -> new UserNotFoundException(issueUpdateDto.getReporterId()));
      issue.setReporter(reporter);
      hasChanges = true;
    }
    if (issueUpdateDto.getAssigneeId() != null) {
      User assigneeFromDto = userRepository.findById(issueUpdateDto.getAssigneeId())
              .orElseThrow(() -> new UserNotFoundException(issueUpdateDto.getAssigneeId()));
      if (issue.getAssignee() != null) {
        if (!assigneeFromDto.getId().equals(issue.getAssignee().getId())) {
          issue.setAssignee(assigneeFromDto);
        }
      } else {
        issue.setAssignee(assigneeFromDto);
      }
      issueHistoryService.recordAssigneeIdHistory(issue, issueUpdateDto);
      hasChanges = true;
    }
    if (!hasChanges) {
      throw new IssueUpdateException(issue.getId());
    }
    issue.setUpdatedAt(Instant.now());
    return issueMapper.toResponseDto(issueRepository.save(issue));
  }

  public void deleteIssueById(Long id) {
    Issue issue = issueRepository
            .findById(id)
            .orElseThrow(() -> new IssueNotFoundException(id));
    issueRepository.delete(issue);
  }
}
