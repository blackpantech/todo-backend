package com.blackpantech.todo.domain.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain service to get, create, edit and delete tasks
 */
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getTask(final long id) {
        return new Task(0, "", false, 1, "", LocalDateTime.now());
    }

    public Task createTask(final String title, final LocalDateTime dueDate) {
        return new Task(0, title, false, 0, "", dueDate);
    }

    public Task editTask(final long id, final String title, final boolean done, final long order, final LocalDateTime dueDate) {
        return new Task(id, title, done, order, "", dueDate);
    }

    public boolean deleteTask(final long id) {
        return false;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>();
    }

    public boolean deleteAllTasks() {
        return false;
    }

    public boolean deleteAllCompletedTasks() {
        return false;
    }

}
