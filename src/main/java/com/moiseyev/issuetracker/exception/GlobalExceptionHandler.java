package com.moiseyev.issuetracker.exception;

import io.jsonwebtoken.JwtException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> accessDeniedExceptionExceptionHandler(AccessDeniedException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<String> jwtExceptionHandler(JwtException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(RegistrationException.class)
  public ResponseEntity<String> registrationException(RegistrationException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> authenticationException(AuthenticationException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(TagNotAssignedException.class)
  public ResponseEntity<String> tagNotAssignedException(TagNotAssignedException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(TagAlreadyAssignedException.class)
  public ResponseEntity<String> tagAlreadyAssignedException(TagAlreadyAssignedException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(TagNotFoundException.class)
  public ResponseEntity<String> tagNotFoundException(TagNotFoundException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CommentNotFoundException.class)
  public ResponseEntity<String> commentNotFoundException(CommentNotFoundException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IssueUpdateException.class)
  public ResponseEntity<String> issueUpdateException(IssueUpdateException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IssueHistoryNotFoundException.class)
  public ResponseEntity<String> issueHistoryNotFoundException(IssueHistoryNotFoundException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EmptyIssueListException.class)
  public ResponseEntity<String> emptyIssueListException(EmptyIssueListException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IssueNotFoundException.class)
  public ResponseEntity<String> issueNotFoudException(IssueNotFoundException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(PriorityNotFoundException.class)
  public ResponseEntity<String> priorityNotFoundException(PriorityNotFoundException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(StatusNotFoundException.class)
  public ResponseEntity<String> statusNotFoundException(StatusNotFoundException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserUpdateException.class)
  public ResponseEntity<String> userUpdateException(UserUpdateException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> userNotFoundException(UserNotFoundException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(LoginAlreadyExistsException.class)
  public ResponseEntity<String> loginAlreadyExistsExceptionHandler(LoginAlreadyExistsException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<String> emailAlreadyExistsException(EmailAlreadyExistsException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(RoleNotFoundException.class)
  public ResponseEntity<String> roleNotFoundException(RoleNotFoundException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<String> validationExceptionHandler(ValidationException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> runtimeExceptionHandler(RuntimeException e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> exceptionHandler(Exception e) {
    log.error("ExceptionHandler: " + e);
    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
  }
}
