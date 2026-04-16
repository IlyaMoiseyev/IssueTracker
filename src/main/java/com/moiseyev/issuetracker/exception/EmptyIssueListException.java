package com.moiseyev.issuetracker.exception;

public class EmptyIssueListException extends RuntimeException {
  public EmptyIssueListException(Integer size) {
    super("Empty issue list exception: list size = " + size);
  }
}
