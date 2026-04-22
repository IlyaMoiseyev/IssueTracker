package com.moiseyev.issuetracker.exception;

public class UserUpdateException extends RuntimeException {
  public UserUpdateException(Long userId) {
    super("No changes. User with id = " + userId + " already has these values");
  }
}
