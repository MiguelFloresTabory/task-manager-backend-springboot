package com.miguel.taskmanager.task_manager_api.dto;

import lombok.Data;

@Data
public class TaskFilter {
    private Long idTask;
    private String[] priorities;
    private String description;
    private Boolean completed;
}
