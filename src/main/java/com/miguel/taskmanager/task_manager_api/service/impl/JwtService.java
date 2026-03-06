package com.miguel.taskmanager.task_manager_api.service.impl;

import com.miguel.taskmanager.task_manager_api.entity.Task;
import com.miguel.taskmanager.task_manager_api.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshExpiration;

    public String extractUsername(final String token){
          try {Claims jwtToken  = Jwts.parser()
                      .verifyWith(getSignInKey())
                      .build()
                      .parseSignedClaims(token)
                      .getPayload();
              return jwtToken.getSubject();
          }catch (ExpiredJwtException err){
            return "JWT Expired";
          }catch (Exception err){
              return null;
          }
    }

    public String generateToken(final User user) {
        return buildToken(user,jwtExpiration);
    }
    public String generateRefreshToken(final User user) {
        return buildToken(user,refreshExpiration);
    }

    private String buildToken(final User user, final long expiration){
        return Jwts.builder()
                .id(user.getId().toString())
                .claims(Map.of("name", user.getName()))
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();

    }
    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean isTokenValid(final String token, final User user){
        final String username = extractUsername(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }
    public boolean isTokenExpired(final String token){
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(final String token){
        final Claims jwtToken  = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return jwtToken.getExpiration();
    }
}
