package com.miguel.taskmanager.task_manager_api.repository.auth;

import com.miguel.taskmanager.task_manager_api.entity.Role;
import com.miguel.taskmanager.task_manager_api.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findById(Long id);
    Optional<Role> findByName(RoleEnum name);
}
