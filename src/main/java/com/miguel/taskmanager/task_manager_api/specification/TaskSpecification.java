package com.miguel.taskmanager.task_manager_api.specification;

import com.miguel.taskmanager.task_manager_api.dto.TaskFilter;
import com.miguel.taskmanager.task_manager_api.entity.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TaskSpecification {

    private TaskSpecification() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<Task> filter(TaskFilter filter) {

        return (root, query, cb) -> {

            if (filter == null) {
                return cb.conjunction(); // retorna true (sin filtros)
            }

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getIdTask() != null) {
                predicates.add(cb.equal(root.get(Task.Fields.idTask), filter.getIdTask()));
            }

            if (filter.getPriorities() != null && filter.getPriorities().length > 0) {

                List<String> validPriorities =
                        List.of(filter.getPriorities()).stream()
                                .filter(Objects::nonNull)
                                .toList();

                if (!validPriorities.isEmpty()) {
                    predicates.add(root.get(Task.Fields.taskPriority).in(validPriorities));
                }
            }

            if (filter.getDescription() != null && !filter.getDescription().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get(Task.Fields.description)),
                                "%" + filter.getDescription().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getCompleted() != null) {
                predicates.add(cb.equal(root.get(Task.Fields.completed), filter.getCompleted()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}