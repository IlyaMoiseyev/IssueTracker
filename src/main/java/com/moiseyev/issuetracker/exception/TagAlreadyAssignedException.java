package com.moiseyev.issuetracker.exception;

public class TagAlreadyAssignedException extends RuntimeException {
  public TagAlreadyAssignedException(String message) {
    super(message);
  }
}
