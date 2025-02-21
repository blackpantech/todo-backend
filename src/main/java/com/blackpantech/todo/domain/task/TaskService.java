package com.blackpantech.todo.domain.task;

import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;

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

    public Task getTask(final long id) throws TaskNotFoundException {
        return taskRepository.getTask(id);
    }

    public Task createTask(final String title, final LocalDateTime dueDate) throws DuplicatedTaskTitleException {
        return taskRepository.createTask(title, dueDate);
    }

    public Task editTask(final long id, final String title, final boolean done, final long order, final LocalDateTime dueDate) throws TaskNotFoundException, DuplicatedTaskTitleException {
        return taskRepository.editTask(id, title, done, order, dueDate);
    }

    public void deleteTask(final long id) throws TaskNotFoundException {
        taskRepository.deleteTask(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public void deleteAllTasks() {
        taskRepository.deleteAllTasks();
    }

    public void deleteAllCompletedTasks() {
        taskRepository.deleteAllCompletedTasks();
    }

}
