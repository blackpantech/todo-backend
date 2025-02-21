package com.blackpantech.todo.domain.task;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain repository to interact with application repository
 */
public interface TaskRepository {

    Task getTask(final long id);

    Task createTask(final String title, final LocalDateTime dueDate);

    Task editTask(final long id, final String title, final boolean done, final long order, final LocalDateTime dueDate);

    boolean deleteTask(final long id);

    List<Task> getAllTasks();

    boolean deleteAllTasks();

    boolean deleteAllCompletedTasks();

}
