package com.moiseyev.issuetracker.exception;

public class IssueUpdateException extends RuntimeException {
  public IssueUpdateException(Long issueId) {
    super("No changes detected. Issue with id = " + issueId + " already has these values");
  }
}
