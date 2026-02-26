package com.miguel.taskmanager.task_manager_api.mapper;

import com.miguel.taskmanager.task_manager_api.dto.TaskCreateDto;
import com.miguel.taskmanager.task_manager_api.dto.TaskResponseDTO;
import com.miguel.taskmanager.task_manager_api.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    // DTO -> Entity
    Task toEntity(TaskCreateDto dto);

    // Entity -> DTO para respuesta completa
    TaskResponseDTO toResponseDto(Task entity);

    // Para updates: actualizar Entity existente desde cualquier DTO
    void updateEntityFromCreateDto(TaskCreateDto dto, @MappingTarget Task entity);
    void updateEntityFromResponseDto(TaskResponseDTO dto, @MappingTarget Task entity);
}