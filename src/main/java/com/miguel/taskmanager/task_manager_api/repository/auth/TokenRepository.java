package com.miguel.taskmanager.task_manager_api.repository.auth;
import com.miguel.taskmanager.task_manager_api.entity.User;
import com.miguel.taskmanager.task_manager_api.entity.auth.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository  extends JpaRepository<Token,Long> {
    List<Token> findAllValidIsFalseOrRevokedIsFalseByUserId(Long idUser);
    Optional<Token> findByToken(String token);


}
