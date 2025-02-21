package com.blackpantech.todo.domain.task;

import java.time.LocalDateTime;
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
        return taskRepository.getTask(id);
    }

    public Task createTask(final String title, final LocalDateTime dueDate) {
        return taskRepository.createTask(title, dueDate);
    }

    public Task editTask(final long id, final String title, final boolean done, final long order, final LocalDateTime dueDate) {
        return taskRepository.editTask(id, title, done, order, dueDate);
    }

    public boolean deleteTask(final long id) {
        return taskRepository.deleteTask(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public boolean deleteAllTasks() {
        return taskRepository.deleteAllTasks();
    }

    public boolean deleteAllCompletedTasks() {
        return taskRepository.deleteAllCompletedTasks();
    }

}
