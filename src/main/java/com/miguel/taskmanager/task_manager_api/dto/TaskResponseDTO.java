package com.miguel.taskmanager.task_manager_api.dto;
import lombok.Data;
import java.time.Instant;
@Data
public class TaskResponseDTO {

    private Long idTask;
    private String title;
    private String description;
    private boolean completed;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant due;
    private String taskPriority;


}