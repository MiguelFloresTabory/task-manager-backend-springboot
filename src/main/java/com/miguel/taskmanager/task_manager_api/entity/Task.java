package com.miguel.taskmanager.task_manager_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_task")
    private Long idTask;
    @Column(name = "title", nullable = false, length = 150)
    private String title;        // Título corto
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;  // Detalle de la tarea
    @Column(name = "completed", nullable = false)
    private boolean completed = false;   // Estado
    @Column(name = "createdAt", nullable = false)
    private Instant createdAt; // Fecha de creación
    @Column(name = "updatedAt", nullable = true)
    private Instant updatedAt; // Última modificación
    @Column(name = "due", nullable = true)
    private Instant due; // Última modificación
    @Enumerated(EnumType.STRING)
    @Column(name = "task_priority", nullable = false)
    private TaskPriority taskPriority;
}

