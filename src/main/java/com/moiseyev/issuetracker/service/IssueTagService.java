package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.EmptyIssueListException;
import com.moiseyev.issuetracker.exception.IssueNotFoundException;
import com.moiseyev.issuetracker.exception.TagAlreadyAssignedException;
import com.moiseyev.issuetracker.exception.TagNotAssignedException;
import com.moiseyev.issuetracker.exception.TagNotFoundException;
import com.moiseyev.issuetracker.model.dto.IssueResponseDto;
import com.moiseyev.issuetracker.model.entity.Issue;
import com.moiseyev.issuetracker.model.entity.IssueTag;
import com.moiseyev.issuetracker.model.entity.Tag;
import com.moiseyev.issuetracker.model.mapper.IssueMapper;
import com.moiseyev.issuetracker.repository.IssueRepository;
import com.moiseyev.issuetracker.repository.IssueTagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueTagService {
  private final IssueTagRepository issueTagRepository;
  private final IssueRepository issueRepository;
  private final TagService tagService;
  private final IssueMapper issueMapper;

  @Autowired
  public IssueTagService(IssueTagRepository issueTagRepository,
                         IssueRepository issueRepository,
                         TagService tagService,
                         IssueMapper issueMapper) {
    this.issueTagRepository = issueTagRepository;
    this.issueRepository = issueRepository;
    this.tagService = tagService;
    this.issueMapper = issueMapper;
  }

  public List<Tag> getTagsForIssue(Long issueId) {
    List<Tag> issueTags = issueTagRepository.findByIssueId(issueId)
            .stream()
            .map(IssueTag::getTag)
            .toList();
    if (issueTags.isEmpty()) {
      throw new TagNotFoundException("Tags for issue with id " + issueId + " not found");
    }
    return issueTags;
  }

  @Transactional
  public List<IssueResponseDto> getIssuesByTag(String tagName) {
    Tag tag = tagService.findTagByName(tagName);
    List<IssueResponseDto> issuesByTagList = issueTagRepository
            .findByTagName(tag.getName())
            .stream()
            .map(issueTag -> issueMapper.toResponseDto(issueTag.getIssue()))
            .toList();
    if (issuesByTagList.isEmpty()) throw new EmptyIssueListException(issuesByTagList.size());
    return issuesByTagList;
  }

  @Transactional
  public void addTagToIssue(Long issueId, String tagName) {
    Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new IssueNotFoundException(issueId));
    Tag tag = tagService.findTagByName(tagName);

    if (issueTagRepository.existsByIssueIdAndTagId(issueId, tag.getId())) {
      throw new TagAlreadyAssignedException("Tag " + tag.getName() + " already assigned for issue with id " + issueId);
    }

    IssueTag issueTag = new IssueTag(null, issue, tag);
    issueTagRepository.save(issueTag);
  }

  @Transactional
  public void removeTagFromIssue(Long issueId, String tagName) {
    if (!issueRepository.existsById(issueId)) {
      throw new IssueNotFoundException(issueId);
    }

    Tag tag = tagService.findTagByName(tagName);

    if (!issueTagRepository.existsByIssueIdAndTagId(issueId, tag.getId())) {
      throw new TagNotAssignedException("Tag " + tag.getName() + " is not assigned to issue with id " + issueId);
    }

    issueTagRepository.deleteByIssueIdAndTagId(issueId, tag.getId());
  }
}
