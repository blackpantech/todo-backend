package com.blackpantech.todo.domain.task;

import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain repository to interact with application repository
 */
public interface TaskRepository {

    Task getTask(final long id) throws TaskNotFoundException;

    Task createTask(final String title, final LocalDateTime dueDate) throws DuplicatedTaskTitleException;

    Task editTask(final long id, final String title, final boolean done, final long order, final LocalDateTime dueDate) throws DuplicatedTaskTitleException, TaskNotFoundException;

    void deleteTask(final long id) throws TaskNotFoundException;

    List<Task> getAllTasks();

    void deleteAllTasks();

    void deleteAllCompletedTasks();

}
