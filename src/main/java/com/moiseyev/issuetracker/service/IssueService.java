package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.EmptyIssueListException;
import com.moiseyev.issuetracker.exception.IssueNotFoundException;
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

  @Autowired
  public IssueService(IssueRepository issueRepository,
                      IssueStatusService issueStatusService,
                      IssuePriorityService issuePriorityService,
                      UserService userService) {
    this.issueRepository = issueRepository;
    this.issueStatusService = issueStatusService;
    this.issuePriorityService = issuePriorityService;
    this.userService = userService;
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
    Issue updatedIssue = getIssueById(issueUpdateDto.getId());

    if (issueUpdateDto.getTitle() != null && !issueUpdateDto.getTitle().isBlank()) {
      updatedIssue.setTitle(issueUpdateDto.getTitle());
    }
    if (issueUpdateDto.getDescription() != null && !issueUpdateDto.getDescription().isBlank()) {
      updatedIssue.setDescription(issueUpdateDto.getDescription());
    }
    if (issueUpdateDto.getStatusType() != null && !issueUpdateDto.getStatusType().isBlank()) {
      updatedIssue.setStatus(issueStatusService.getIssueStatusByName(issueUpdateDto.getStatusType()));
    }
    if (issueUpdateDto.getReporterId() != null) {
      updatedIssue.setReporter(userService.getUserById(issueUpdateDto.getReporterId()));
    }
    if (issueUpdateDto.getAssigneeId() != null) {
      updatedIssue.setAssignee(userService.getUserById(issueUpdateDto.getAssigneeId()));
    }
    updatedIssue.setUpdatedAt(Instant.now());
    return issueRepository.save(updatedIssue);
  }

  public void deleteIssueById(Long id) {
    getIssueById(id);
    issueRepository.deleteById(id);
  }
}
