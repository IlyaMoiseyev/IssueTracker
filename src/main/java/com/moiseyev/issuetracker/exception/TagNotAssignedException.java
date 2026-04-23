package com.moiseyev.issuetracker.exception;

public class TagNotAssignedException extends RuntimeException {
  public TagNotAssignedException(String message) {
    super(message);
  }
}
