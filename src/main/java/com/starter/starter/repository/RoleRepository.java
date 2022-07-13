package com.starter.starter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starter.starter.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Boolean existsByName(String name);

    Optional<Role> findByName(String name);
}
