package com.miguel.taskmanager.task_manager_api.dto.auth;

import com.miguel.taskmanager.task_manager_api.entity.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private Long idUser;
    private String name;
    private String email;
    private String password;
    private String dni;
    private String number;
    private RoleEnum role;

}

