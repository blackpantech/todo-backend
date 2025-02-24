package com.blackpantech.todo.domain.task;

import java.time.LocalDateTime;

/**
 * Domain record for task objects
 *
 * @param id task id
 * @param title task title
 * @param completed completion of the task
 * @param order task position in list
 * @param dueDate task due date
 */
public record Task(long id, String title, boolean completed, long order, LocalDateTime dueDate) {

}
