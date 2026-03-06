package com.miguel.taskmanager.task_manager_api.dto;


import com.miguel.taskmanager.task_manager_api.entity.enums.RoleEnum;
import lombok.Data;

@Data
public class RoleCreateDTO {

    private RoleEnum name;

    private String description;

}