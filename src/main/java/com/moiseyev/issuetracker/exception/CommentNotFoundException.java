package com.moiseyev.issuetracker.exception;

public class CommentNotFoundException extends RuntimeException {
  public CommentNotFoundException(Long commentId) {
    super("Comment with id = " + commentId + " not found");
  }

  public CommentNotFoundException(String message) {
    super(message);
  }
}
