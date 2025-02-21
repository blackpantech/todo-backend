package com.blackpantech.todo.infra.jpa;

import com.blackpantech.todo.domain.task.Task;
import com.blackpantech.todo.domain.task.TaskRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JpaTaskRepository implements TaskRepository {

    private final TaskJpaRepository taskJpaRepository;

    public JpaTaskRepository(final TaskJpaRepository taskJpaRepository) {
        this.taskJpaRepository = taskJpaRepository;
    }

    @Override
    public Task getTask(final long id) {
        return null;
    }

    @Override
    public Task createTask(final String title, final LocalDateTime dueDate) {
        return null;
    }

    @Override
    public Task editTask(final long id, final String title, final boolean done, final long order, final LocalDateTime dueDate) {
        return null;
    }

    @Override
    public boolean deleteTask(final long id) {
        return false;
    }

    @Override
    public List<Task> getAllTasks() {
        return List.of();
    }

    @Override
    public boolean deleteAllTasks() {
        return false;
    }

    @Override
    public boolean deleteAllCompletedTasks() {
        return false;
    }

}
