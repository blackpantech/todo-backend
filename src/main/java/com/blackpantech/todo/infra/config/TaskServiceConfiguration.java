package com.blackpantech.todo.infra.config;

import com.blackpantech.todo.domain.task.TaskRepository;
import com.blackpantech.todo.domain.task.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for bean factories
 */
@Configuration
public class TaskServiceConfiguration {

    /**
     * Bean factory for domain service
     *
     * @param taskRepository task repository
     *
     * @return task service bean
     */
    @Bean
    public TaskService taskService(final TaskRepository taskRepository) {
        return new TaskService(taskRepository);
    }

}
