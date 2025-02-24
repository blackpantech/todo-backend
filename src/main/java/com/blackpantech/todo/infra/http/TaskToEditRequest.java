package com.blackpantech.todo.infra.http;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Record to map task edit request
 *
 * @param title edited task title
 * @param completed edited task completion
 * @param order edited position of the task in the list
 * @param dueDate edited task due date
 */
public record TaskToEditRequest(@NotBlank String title,
                                @NotNull boolean completed,
                                @Min(1) long order,
                                LocalDateTime dueDate) {

}
