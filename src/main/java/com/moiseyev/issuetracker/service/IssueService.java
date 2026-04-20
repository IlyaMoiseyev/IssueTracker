package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.EmptyIssueListException;
import com.moiseyev.issuetracker.exception.IssueNotFoundException;
import com.moiseyev.issuetracker.exception.IssueUpdateException;
import com.moiseyev.issuetracker.model.dto.IssueCreateDto;
import com.moiseyev.issuetracker.model.dto.IssueUpdateDto;
import com.moiseyev.issuetracker.model.entity.Issue;
import com.moiseyev.issuetracker.model.entity.IssuePriority;
import com.moiseyev.issuetracker.model.entity.IssueStatus;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.repository.IssueRepository;
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
  private final UserService userService;
  private final IssueHistoryService issueHistoryService;

  @Autowired
  public IssueService(IssueRepository issueRepository,
                      IssueStatusService issueStatusService,
                      IssuePriorityService issuePriorityService,
                      UserService userService,
                      IssueHistoryService issueHistoryService) {
    this.issueRepository = issueRepository;
    this.issueStatusService = issueStatusService;
    this.issuePriorityService = issuePriorityService;
    this.userService = userService;
    this.issueHistoryService = issueHistoryService;
  }

  public List<Issue> getAllIssues() {
    List<Issue> issues = issueRepository.findAll();
    if (issues.isEmpty()) throw new EmptyIssueListException(issues.size());
    return issueRepository.findAll();
  }

  public Issue getIssueById(Long id) {
    return issueRepository.findById(id).orElseThrow(() -> new IssueNotFoundException(id));
  }

  @Transactional
  public Issue createIssue(IssueCreateDto issueCreateDto) {
    IssueStatus status = issueStatusService.getIssueStatusByName(issueCreateDto.getStatus());
    IssuePriority priority = issuePriorityService.getIssuePriorityByName(issueCreateDto.getPriority());
    User reporter = userService.getUserById(issueCreateDto.getReporterId());
    User assignee = issueCreateDto.getAssigneeId() != null
            ? userService.getUserById(issueCreateDto.getAssigneeId())
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

    return issueRepository.save(issue);
  }

  @Transactional
  public Issue updateIssue(IssueUpdateDto issueUpdateDto) {
    Issue issue = getIssueById(issueUpdateDto.getId());
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
      issue.setReporter(userService.getUserById(issueUpdateDto.getReporterId()));
      hasChanges = true;
    }
    if (issueUpdateDto.getAssigneeId() != null) {
      User assigneeFromDto = userService.getUserById(issueUpdateDto.getAssigneeId());
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
    return issueRepository.save(issue);
  }

  public void deleteIssueById(Long id) {
    getIssueById(id);
    issueRepository.deleteById(id);
  }
}
