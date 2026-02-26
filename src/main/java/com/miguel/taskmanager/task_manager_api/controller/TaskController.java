package com.miguel.taskmanager.task_manager_api.controller;

import com.miguel.taskmanager.task_manager_api.dto.*;
import com.miguel.taskmanager.task_manager_api.service.TaskService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // POST
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @RequestBody TaskCreateDto task) {

        TaskResponseDTO newTask = taskService.createTask(task);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newTask);
    }

    // PUT
    @PutMapping("/{idTask}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long idTask,
            @RequestBody TaskUpdateDTO task) {

        TaskResponseDTO updatedTask =
                taskService.updateTask(idTask, task);

        return ResponseEntity.ok(updatedTask);
    }

    // GET
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTask(
            TaskFilter taskFilter) {

        List<TaskResponseDTO> tasks =
                taskService.getAllTasksByFilters(taskFilter);

        return ResponseEntity.ok(tasks);
    }

    // DELETE
    @DeleteMapping("/{idTask}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long idTask) {

        taskService.deleteTask(idTask);

        return ResponseEntity.noContent().build();
    }
}