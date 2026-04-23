package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.TagNotFoundException;
import com.moiseyev.issuetracker.model.entity.Tag;
import com.moiseyev.issuetracker.model.enums.TagType;
import com.moiseyev.issuetracker.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
  private final TagRepository tagRepository;

  @Autowired
  public TagService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public List<Tag> findAllTags() {
    return tagRepository.findAll();
  }

  public Tag getTagById(Integer id) {
    return tagRepository.findById(id)
            .orElseThrow(() -> new TagNotFoundException(id));
  }

  public Tag findTagByName(String tagName) {
    if (tagName == null || tagName.isEmpty()) {
      throw new TagNotFoundException(tagName);
    }
    TagType tagType = convertToTagType(tagName);

    return tagRepository.findTagByName(tagType)
            .orElseThrow(() -> new TagNotFoundException("Tag with name " + tagType.name() + " not found"));
  }

  private TagType convertToTagType(String tagName) {
    String preparedTagName = tagName.trim().toUpperCase();
    try {
      return TagType.valueOf(preparedTagName);
    } catch (IllegalArgumentException e) {
      throw new TagNotFoundException("Tag with name " + tagName + " not found");
    }
  }
}
