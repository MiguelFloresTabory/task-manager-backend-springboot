package com.miguel.taskmanager.task_manager_api.service.impl;

import com.miguel.taskmanager.task_manager_api.dto.auth.LoginRequest;
import com.miguel.taskmanager.task_manager_api.dto.auth.RegisterRequest;
import com.miguel.taskmanager.task_manager_api.dto.auth.TokenResponse;
import com.miguel.taskmanager.task_manager_api.entity.User;
import com.miguel.taskmanager.task_manager_api.entity.auth.Token;
import com.miguel.taskmanager.task_manager_api.repository.auth.TokenRepository;
import com.miguel.taskmanager.task_manager_api.repository.auth.UserRepository;
import com.miguel.taskmanager.task_manager_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceI implements AuthService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register (RegisterRequest request){
      User user = User.builder()
              .name(request.getName())
              .email(request.getEmail())
              .password(passwordEncoder.encode(request.getPassword()))
              .build();

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
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw  new IllegalArgumentException("Invalid Bearer token");
        }
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail == null){
             throw new IllegalArgumentException("Invalid Refresh Token");
        }
        final User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));
        if(!jwtService.isTokenValid(refreshToken, user)){
            throw  new IllegalArgumentException("Invalid Refresh Token");
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
