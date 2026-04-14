package com.moiseyev.issuetracker.exception;

public class RoleNotFoundException extends RuntimeException {
  public RoleNotFoundException(Integer id) {
    super("Role with id=" + id + " not found");
  }

  public RoleNotFoundException(String name) {
    super("Role with name=" + name + " not found");
  }
}
