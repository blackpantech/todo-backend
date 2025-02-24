package com.blackpantech.todo.infra.http;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record TaskToCreateRequest(@NotBlank String title, LocalDateTime dueDate) {

}
