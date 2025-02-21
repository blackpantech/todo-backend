package com.blackpantech.todo.domain.task;

import java.time.LocalDateTime;

/**
 * Domain record for task objects
 * @param id task id
 * @param title task title
 * @param done completion of the task
 * @param order task position in list
 * @param url task URL
 * @param dueDate task due date
 */
public record Task(long id, String title, boolean done, long order, String url, LocalDateTime dueDate) {

}
