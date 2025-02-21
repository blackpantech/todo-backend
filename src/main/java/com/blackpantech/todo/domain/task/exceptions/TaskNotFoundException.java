package com.blackpantech.todo.domain.task.exceptions;

public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(final long id) {
        super(String.format("Task with id %d not found", id));
    }

}
