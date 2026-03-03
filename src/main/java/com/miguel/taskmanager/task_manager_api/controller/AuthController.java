package com.miguel.taskmanager.task_manager_api.controller;

import com.miguel.taskmanager.task_manager_api.dto.auth.LoginRequest;
import com.miguel.taskmanager.task_manager_api.dto.auth.RegisterRequest;
import com.miguel.taskmanager.task_manager_api.dto.auth.TokenResponse;
import com.miguel.taskmanager.task_manager_api.service.AuthService;
import com.miguel.taskmanager.task_manager_api.service.impl.AuthServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceI service;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody final RegisterRequest request){
        final TokenResponse token = service.register(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody final LoginRequest request){
        final TokenResponse token = service.login(request);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader){
        final TokenResponse token = service.refreshToken(authHeader);
        return ResponseEntity.ok(token);
    }


}
