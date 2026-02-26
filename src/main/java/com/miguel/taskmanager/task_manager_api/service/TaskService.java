package com.miguel.taskmanager.task_manager_api.service;

import com.miguel.taskmanager.task_manager_api.dto.TaskCreateDto;
import com.miguel.taskmanager.task_manager_api.dto.TaskFilter;
import com.miguel.taskmanager.task_manager_api.dto.TaskResponseDTO;
import com.miguel.taskmanager.task_manager_api.dto.TaskUpdateDTO;
import java.util.List;

public interface TaskService {

    List<TaskResponseDTO> getAllTasksByFilters(TaskFilter taskfilter);
    TaskResponseDTO createTask(TaskCreateDto task);
    TaskResponseDTO updateTask(Long taskId, TaskUpdateDTO task);
    void deleteTask(Long taskId);

}
