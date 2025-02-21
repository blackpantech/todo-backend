package com.blackpantech.todo.domain.task.exceptions;

public class DuplicatedTaskTitleException extends Exception {

    public DuplicatedTaskTitleException(final String title) {
        super(String.format("Task with title %s already exists", title));
    }

}
