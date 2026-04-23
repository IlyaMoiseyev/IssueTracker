package com.moiseyev.issuetracker.exception;

public class TagNotFoundException extends RuntimeException {
  public TagNotFoundException(String message) {
    super(message);
  }

  public TagNotFoundException(Integer id) {
    super("Tag with id = " + id + " not found");
  }
}
