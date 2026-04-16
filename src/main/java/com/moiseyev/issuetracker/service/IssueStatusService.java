package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.StatusNotFoundException;
import com.moiseyev.issuetracker.model.entity.IssueStatus;
import com.moiseyev.issuetracker.model.enums.StatusType;
import com.moiseyev.issuetracker.repository.IssueStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IssueStatusService {
  private final IssueStatusRepository issueStatusRepository;

  @Autowired
  public IssueStatusService(IssueStatusRepository issueStatusRepository) {
    this.issueStatusRepository = issueStatusRepository;
  }

  public List<IssueStatus> findAllIssueStatuses() {
    return issueStatusRepository.findAll();
  }

  public IssueStatus getIssueStatusById(Integer id) {
    return issueStatusRepository.findById(id)
            .orElseThrow(() -> new StatusNotFoundException(id));
  }

  public IssueStatus getIssueStatusByName(String statusName) {
    if (statusName == null || statusName.isEmpty()) {
      throw new StatusNotFoundException(statusName);
    }
    StatusType statusType = convertToStatusType(statusName);

    return issueStatusRepository.findByName(statusType)
            .orElseThrow(() -> new StatusNotFoundException(statusType.name()));
  }

  private StatusType convertToStatusType(String name) {
    String preparedName = name.trim().toUpperCase();
    try {
      return StatusType.valueOf(preparedName);
    } catch (IllegalArgumentException e) {
      throw new StatusNotFoundException(name);
    }
  }


}
