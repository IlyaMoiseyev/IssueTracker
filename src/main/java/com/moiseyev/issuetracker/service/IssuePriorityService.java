package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.PriorityNotFoundException;
import com.moiseyev.issuetracker.model.entity.IssuePriority;
import com.moiseyev.issuetracker.model.enums.PriorityLevel;
import com.moiseyev.issuetracker.repository.IssuePriorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IssuePriorityService {
  private final IssuePriorityRepository priorityRepository;

  @Autowired
  public IssuePriorityService(IssuePriorityRepository priorityRepository) {
    this.priorityRepository = priorityRepository;
  }

  public List<IssuePriority> findAllIssuePriorities() {
    return priorityRepository.findAll();
  }

  public IssuePriority getIssuePriorityById(Integer id) {
    return priorityRepository.findById(id)
            .orElseThrow(() -> new PriorityNotFoundException(id));
  }

  public IssuePriority getIssuePriorityByName(String priorityName) {
    if (priorityName == null || priorityName.isEmpty()) {
      throw new PriorityNotFoundException(priorityName);
    }
    PriorityLevel priorityLevelName = convertToPriorityLevel(priorityName);

    return priorityRepository.findByName(priorityLevelName)
            .orElseThrow(() -> new PriorityNotFoundException(priorityName));
  }

  private PriorityLevel convertToPriorityLevel(String priorityName) {
    String preparedPriorityName = priorityName.trim().toUpperCase();
    try {
      return PriorityLevel.valueOf(preparedPriorityName);
    } catch (IllegalArgumentException e) {
      throw new PriorityNotFoundException(priorityName);
    }
  }
}
