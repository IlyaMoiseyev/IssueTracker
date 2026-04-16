package com.moiseyev.issuetracker.exception;

public class StatusNotFoundException extends RuntimeException {
  public StatusNotFoundException(Integer id) {
    super("Status with id = " + id + " not found");
  }

  public StatusNotFoundException(String name) {
    super("Status with name = " + name + " not found");
  }
}
