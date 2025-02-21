package com.blackpantech.todo.infra.config;

import com.blackpantech.todo.domain.task.TaskRepository;
import com.blackpantech.todo.domain.task.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskServiceConfiguration {

    @Bean
    public TaskService taskService(final TaskRepository taskRepository) {
        return new TaskService(taskRepository);
    }

}
