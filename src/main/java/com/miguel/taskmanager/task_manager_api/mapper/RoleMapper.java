package com.miguel.taskmanager.task_manager_api.mapper;

import com.miguel.taskmanager.task_manager_api.dto.RoleCreateDTO;
import com.miguel.taskmanager.task_manager_api.dto.RoleResponseDTO;
import com.miguel.taskmanager.task_manager_api.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    // DTO -> Entity
    Role toEntity(RoleCreateDTO dto);

    // Entity -> DTO para respuesta
    RoleResponseDTO toResponseDto(Role entity);

    // Actualizar Entity desde DTO
    void updateEntityFromCreateDto(RoleCreateDTO dto, @MappingTarget Role entity);

    void updateEntityFromResponseDto(RoleResponseDTO dto, @MappingTarget Role entity);
}