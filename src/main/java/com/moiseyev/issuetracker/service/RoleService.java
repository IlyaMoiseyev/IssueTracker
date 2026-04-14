package com.moiseyev.issuetracker.service;

import com.moiseyev.issuetracker.exception.RoleNotFoundException;
import com.moiseyev.issuetracker.model.entity.Role;
import com.moiseyev.issuetracker.model.enums.RoleType;
import com.moiseyev.issuetracker.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
  private final RoleRepository roleRepository;

  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public List<Role> findAllRoles() {
    return roleRepository.findAll();
  }

  public Role getRoleById(Integer id) {
    return roleRepository.findById(id)
            .orElseThrow(() -> new RoleNotFoundException(id));
  }

  public Role findRoleByName(String roleName) {
    if (roleName == null || roleName.isEmpty()) {
      throw new RoleNotFoundException(roleName);
    }
    RoleType roleType = convertToRoleType(roleName);

    return roleRepository.findRoleByName(roleType)
            .orElseThrow(() -> new RoleNotFoundException(roleType.name()));
  }

  private RoleType convertToRoleType(String roleName) {
    String preparedRoleName = roleName.trim().toUpperCase();
    try {
      return RoleType.valueOf(preparedRoleName);
    } catch (IllegalArgumentException e) {
      throw new RoleNotFoundException(roleName);
    }
  }
}
