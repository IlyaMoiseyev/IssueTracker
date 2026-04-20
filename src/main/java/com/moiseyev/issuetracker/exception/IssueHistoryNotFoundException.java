package com.moiseyev.issuetracker.exception;

public class IssueHistoryNotFoundException extends RuntimeException {
  public IssueHistoryNotFoundException(Long id) {
    super("History records for issue with id = " + id + " not found");
  }
}
