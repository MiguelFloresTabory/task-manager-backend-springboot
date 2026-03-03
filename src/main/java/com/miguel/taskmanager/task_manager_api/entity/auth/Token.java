package com.miguel.taskmanager.task_manager_api.entity.auth;

import com.miguel.taskmanager.task_manager_api.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tokens")
public class Token {
    public enum TokenType{
        BEARER
    }
    @Id
    @GeneratedValue
    public Long id;
    @Column(unique = true)
    public String token;
    @Enumerated
    public TokenType tokenType = TokenType.BEARER;
    public boolean revoked;
    public boolean expired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    public User user;
}
