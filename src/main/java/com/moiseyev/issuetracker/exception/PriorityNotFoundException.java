package com.moiseyev.issuetracker.exception;

public class PriorityNotFoundException extends RuntimeException {
  public PriorityNotFoundException(Integer id) {
    super("Priority with id = " + id + " not found");
  }

  public PriorityNotFoundException(String name) {
    super("Priority with name = " + name + " not found");
  }
}
