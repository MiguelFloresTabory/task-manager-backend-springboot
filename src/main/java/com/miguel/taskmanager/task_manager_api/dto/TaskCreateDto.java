package com.miguel.taskmanager.task_manager_api.dto;

import com.miguel.taskmanager.task_manager_api.entity.enums.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateDto {

    private String title;
    private String description;
    private boolean completed;
    private TaskPriority taskPriority;
    private String due;
}