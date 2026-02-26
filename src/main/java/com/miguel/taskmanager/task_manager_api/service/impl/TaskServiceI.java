package com.miguel.taskmanager.task_manager_api.service.impl;

import com.miguel.taskmanager.task_manager_api.dto.*;
import com.miguel.taskmanager.task_manager_api.entity.Task;
import com.miguel.taskmanager.task_manager_api.mapper.TaskMapper;
import com.miguel.taskmanager.task_manager_api.repository.TaskRepository;
import com.miguel.taskmanager.task_manager_api.service.TaskService;
import com.miguel.taskmanager.task_manager_api.specification.TaskSpecification;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceI implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceI(TaskRepository taskRepository,
                        TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    // GET
    @Override
    public List<TaskResponseDTO> getAllTasksByFilters(TaskFilter taskFilter) {
        List<Task> tasks =
                taskRepository.findAll(TaskSpecification.filter(taskFilter));

        return tasks.stream()
                .map(taskMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // POST
    @Override
    public TaskResponseDTO createTask(TaskCreateDto taskCreate) {

        Task newTask = new Task();
        newTask.setDue(Instant.parse(taskCreate.getDue()));
        newTask.setCompleted(taskCreate.isCompleted());
        newTask.setDescription(taskCreate.getDescription());
        newTask.setTitle(taskCreate.getTitle());
        newTask.setCreatedAt(Instant.now());
        newTask.setTaskPriority(taskCreate.getTaskPriority());

        return taskMapper.toResponseDto(taskRepository.save(newTask));
    }

    // PUT
    @Override
    public TaskResponseDTO updateTask(Long taskId, TaskUpdateDTO task) {

        Task taskUpdate = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Task not found with id: " + taskId));

        taskUpdate.setTitle(task.getTitle());
        taskUpdate.setDue(task.getDue());
        taskUpdate.setCompleted(task.isCompleted());
        taskUpdate.setDescription(task.getDescription());
        taskUpdate.setCreatedAt(task.getCreatedAt());
        taskUpdate.setUpdatedAt(Instant.now());
        taskUpdate.setTaskPriority(task.getTaskPriority());

        return taskMapper.toResponseDto(taskRepository.save(taskUpdate));
    }

    // DELETE
    @Override
    public void deleteTask(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Task not found with id: " + taskId));

        taskRepository.delete(task);
    }
}