package com.blackpantech.todo.infra.http;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Record to map task creation request
 *
 * @param title new task title
 * @param dueDate new task due date
 */
public record TaskToCreateRequest(@NotBlank String title, LocalDateTime dueDate) {

}
