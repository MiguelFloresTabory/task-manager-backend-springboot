package com.miguel.taskmanager.task_manager_api.service.impl;

import com.miguel.taskmanager.task_manager_api.dto.auth.LoginRequest;
import com.miguel.taskmanager.task_manager_api.dto.auth.RegisterRequest;
import com.miguel.taskmanager.task_manager_api.dto.auth.TokenResponse;
import com.miguel.taskmanager.task_manager_api.entity.Role;
import com.miguel.taskmanager.task_manager_api.entity.User;
import com.miguel.taskmanager.task_manager_api.entity.auth.Token;
import com.miguel.taskmanager.task_manager_api.entity.enums.RoleEnum;
import com.miguel.taskmanager.task_manager_api.repository.auth.RoleRepository;
import com.miguel.taskmanager.task_manager_api.repository.auth.TokenRepository;
import com.miguel.taskmanager.task_manager_api.repository.auth.UserRepository;
import com.miguel.taskmanager.task_manager_api.service.AuthService;
import com.miguel.taskmanager.task_manager_api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceI implements AuthService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public TokenResponse register (RegisterRequest request){
      User user = User.builder()
              .name(request.getName())
              .dni(request.getDni())
              .number(request.getNumber())
              .email(request.getEmail())
              .password(passwordEncoder.encode(request.getPassword()))
              .build();
      //añadimos el rol
      Role role = roleRepository.findByName(RoleEnum.USER).orElseThrow(() ->   new ResponseStatusException(HttpStatus.UNAUTHORIZED, "no_role"));
      user.setRole(role);
      User saveUser = userRepository.save(user);
      String jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);
      saveUserToken(saveUser, jwtToken);
      return new TokenResponse(jwtToken, refreshToken);
     }
    //De forma interna es necesario que Spring sepa de que forma verificara
     public TokenResponse login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
         saveUserToken(user, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
     }

    public void saveUserToken(User user, String jwtToken){
        Token token = Token.builder().user(user).token(jwtToken).tokenType(Token.TokenType.BEARER)
                .expired(false).revoked(false).build();
        tokenRepository.save(token);

    }

    private void revokeAllUserTokens(final User user){
        final List<Token> validUserTokens = tokenRepository.findAllValidIsFalseOrRevokedIsFalseByUserId(user.getId());
        if(!validUserTokens.isEmpty()){
            for(final Token token : validUserTokens){
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }
    //refreshToken
    public TokenResponse refreshToken(final String authHeader){
        String msg = "invalid_token";
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, msg);
        }
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail.equals( "JWT Expired")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token_refresh_expired");
        }
        if(userEmail == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, msg);
        }
        final User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));
        if(!jwtService.isTokenValid(refreshToken, user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, msg);
        }
        final String accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        return new TokenResponse(accessToken, refreshToken);

    }
    // --- LOGOUT ---
    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header missing or invalid");
        }

        String jwtToken = authHeader.substring(7);

        Token token = tokenRepository.findByToken(jwtToken)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));

        // Revocar y expirar el token
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);
    }


}
