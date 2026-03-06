package com.miguel.taskmanager.task_manager_api.service;

import com.miguel.taskmanager.task_manager_api.dto.RoleCreateDTO;
import com.miguel.taskmanager.task_manager_api.dto.RoleResponseDTO;
import com.miguel.taskmanager.task_manager_api.dto.RoleUpdateDTO;

import java.util.List;

public interface RoleService {

    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO getRoleById(Long roleId);

    RoleResponseDTO createRole(RoleCreateDTO roleCreate);

    RoleResponseDTO updateRole(Long roleId, RoleUpdateDTO roleUpdate);


    void deleteRole(Long roleId);
}