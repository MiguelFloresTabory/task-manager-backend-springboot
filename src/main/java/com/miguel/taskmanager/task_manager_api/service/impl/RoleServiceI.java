package com.miguel.taskmanager.task_manager_api.service.impl;

import com.miguel.taskmanager.task_manager_api.dto.RoleCreateDTO;
import com.miguel.taskmanager.task_manager_api.dto.RoleResponseDTO;
import com.miguel.taskmanager.task_manager_api.dto.RoleUpdateDTO;
import com.miguel.taskmanager.task_manager_api.entity.Role;
import com.miguel.taskmanager.task_manager_api.mapper.RoleMapper;
import com.miguel.taskmanager.task_manager_api.repository.auth.RoleRepository;
import com.miguel.taskmanager.task_manager_api.service.RoleService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceI implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceI(RoleRepository roleRepository,
                           RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    // GET
    @Override
    public List<RoleResponseDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(roleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // GET BY ID
    @Override
    public RoleResponseDTO getRoleById(Long roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found with id: " + roleId));

        return roleMapper.toResponseDto(role);
    }

    // POST
    @Override
    public RoleResponseDTO createRole(RoleCreateDTO roleCreate) {

        Role role = new Role();
        role.setName(roleCreate.getName());
        role.setDescription(roleCreate.getDescription());

        return roleMapper.toResponseDto(roleRepository.save(role));
    }

    // PUT
    @Override
    public RoleResponseDTO updateRole(Long roleId, RoleUpdateDTO roleUpdate) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found with id: " + roleId));

        role.setName(roleUpdate.getName());
        role.setDescription(roleUpdate.getDescription());

        return roleMapper.toResponseDto(roleRepository.save(role));
    }

    // DELETE
    @Override
    public void deleteRole(Long roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found with id: " + roleId));

        roleRepository.delete(role);
    }
}