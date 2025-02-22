package com.blackpantech.todo.domain.task.exceptions;

/**
 * Checked exception in case the title of a task is already taken
 */
public class DuplicatedTaskTitleException extends Exception {

    public DuplicatedTaskTitleException(final String title) {
        super(String.format("Task with title %s already exists", title));
    }

}
