package com.miguel.taskmanager.task_manager_api.dto;

import com.miguel.taskmanager.task_manager_api.entity.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDTO {
    private String title;
    private String description;
    private boolean completed;
    private TaskPriority taskPriority;
    private Instant due;
    private Instant createdAt; // Fecha de creación
}
