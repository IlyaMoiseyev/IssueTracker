package com.moiseyev.issuetracker.repository;

import com.moiseyev.issuetracker.model.entity.Role;
import com.moiseyev.issuetracker.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findRoleByName(RoleType name);
}
