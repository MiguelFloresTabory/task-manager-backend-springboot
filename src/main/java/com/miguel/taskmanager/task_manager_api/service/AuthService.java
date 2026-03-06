package com.miguel.taskmanager.task_manager_api.service;

import com.miguel.taskmanager.task_manager_api.dto.auth.LoginRequest;
import com.miguel.taskmanager.task_manager_api.dto.auth.RegisterRequest;
import com.miguel.taskmanager.task_manager_api.dto.auth.TokenResponse;
import com.miguel.taskmanager.task_manager_api.entity.User;

public interface AuthService {

    // TaskResponseDTO updateTask(Long taskId, TaskUpdateDTO task);
    TokenResponse register (RegisterRequest request);
    TokenResponse login(LoginRequest request);
    void saveUserToken(User user, String jwtToken);
    TokenResponse refreshToken(final String authHeader);
    public void logout(String authHeader);
}
