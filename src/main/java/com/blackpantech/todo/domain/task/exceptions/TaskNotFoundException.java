package com.blackpantech.todo.domain.task.exceptions;

/**
 * Checked exception in case a task is not found
 */
public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(final long id) {
        super(String.format("Task with id %d not found", id));
    }

}
