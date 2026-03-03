package com.miguel.taskmanager.task_manager_api.repository.auth;
import com.miguel.taskmanager.task_manager_api.entity.Task;
import com.miguel.taskmanager.task_manager_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
