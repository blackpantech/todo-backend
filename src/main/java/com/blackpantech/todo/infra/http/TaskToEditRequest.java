package com.blackpantech.todo.infra.http;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskToEditRequest(@NotBlank String title,
                                @NotNull boolean completed,
                                @Min(1) long order,
                                LocalDateTime dueDate) {

}
