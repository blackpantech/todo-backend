package com.blackpantech.todo.domain.task;

import java.time.LocalDateTime;

public record Task(long id, String title, boolean done, long order, String url, LocalDateTime dueDate) {

}
