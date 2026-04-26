package com.moiseyev.issuetracker.security;

import com.moiseyev.issuetracker.exception.CommentNotFoundException;
import com.moiseyev.issuetracker.exception.IssueHistoryNotFoundException;
import com.moiseyev.issuetracker.exception.IssueNotFoundException;
import com.moiseyev.issuetracker.model.entity.Comment;
import com.moiseyev.issuetracker.model.entity.Issue;
import com.moiseyev.issuetracker.model.entity.IssueHistory;
import com.moiseyev.issuetracker.model.entity.User;
import com.moiseyev.issuetracker.model.enums.RoleType;
import com.moiseyev.issuetracker.repository.CommentRepository;
import com.moiseyev.issuetracker.repository.IssueHistoryRepository;
import com.moiseyev.issuetracker.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("accessService")
public class AccessService {

  private final AuthUserService authUserService;
  private final IssueRepository issueRepository;
  private final CommentRepository commentRepository;
  private final IssueHistoryRepository historyRepository;

  @Autowired
  public AccessService(AuthUserService authUserService,
                       IssueRepository issueRepository,
                       CommentRepository commentRepository,
                       IssueHistoryRepository historyRepository) {
    this.authUserService = authUserService;
    this.issueRepository = issueRepository;
    this.commentRepository = commentRepository;
    this.historyRepository = historyRepository;
  }

  public boolean canViewIssue(Long issueId) {
    User currentUser = authUserService.getCurrentUser();

    if (isAdmin(currentUser)) return true;

    Issue issue = issueRepository.findById(issueId)
            .orElseThrow(() -> new IssueNotFoundException(issueId));

    return isReporter(issue, currentUser) || isAssignee(issue, currentUser);
  }

  public boolean canModifyIssue(Long issueId) {
    User currentUser = authUserService.getCurrentUser();

    if (isAdmin(currentUser)) return true;

    Issue issue = issueRepository.findById(issueId)
            .orElseThrow(() -> new IssueNotFoundException(issueId));

    if (currentUser.getRole().getName() == RoleType.DEVELOPER) {
      return isReporter(issue, currentUser) || isAssignee(issue, currentUser);
    }

    if (currentUser.getRole().getName() == RoleType.REPORTER) {
      return isReporter(issue, currentUser);
    }

    return false;
  }

  public boolean canViewComment(Long commentId) {
    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(commentId));

    return canViewIssue(comment.getIssue().getId());
  }

  public boolean canModifyComment(Long commentId) {
    User currentUser = authUserService.getCurrentUser();

    if (isAdmin(currentUser)) return true;

    Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(commentId));

    return comment.getAuthor().getId().equals(currentUser.getId());
  }

  public boolean canViewHistoryRecord(Long historyId) {
    IssueHistory history = historyRepository.findById(historyId)
            .orElseThrow(() -> new IssueHistoryNotFoundException(historyId));

    return canViewIssue(history.getIssue().getId());
  }

  public boolean canViewHistoryForIssue(Long issueId) {
    return canViewIssue(issueId);
  }

  public boolean canViewTags(Long issueId) {
    return canViewIssue(issueId);
  }

  public boolean canManageTags(Long issueId) {
    User currentUser = authUserService.getCurrentUser();

    if (isAdmin(currentUser)) return true;

    Issue issue = issueRepository.findById(issueId)
            .orElseThrow(() -> new IssueNotFoundException(issueId));

    return isReporter(issue, currentUser);
  }

  private boolean isAdmin(User user) {
    return user.getRole().getName() == RoleType.ADMIN;
  }

  private boolean isReporter(Issue issue, User user) {
    return issue.getReporter() != null &&
            issue.getReporter().getId().equals(user.getId());
  }

  private boolean isAssignee(Issue issue, User user) {
    return issue.getAssignee() != null &&
            issue.getAssignee().getId().equals(user.getId());
  }
}
