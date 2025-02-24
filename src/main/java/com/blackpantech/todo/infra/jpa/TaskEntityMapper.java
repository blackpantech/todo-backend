package com.blackpantech.todo.infra.jpa;

import com.blackpantech.todo.domain.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapstruct mapper JPA Task Object -> Domain Task Object
 */
@Mapper(componentModel = "spring")
public interface TaskEntityMapper {

    /**
     * Mapper JPA Task Object -> Domain Task Object
     *
     * @param taskEntity JPA Task Object
     *
     * @return Domain Task Object
     */
    @Mapping(source = "taskEntity.orderPosition", target = "order")
    Task TaskEntityToTask(TaskEntity taskEntity);

    /**
     * Mapper list of JPA Task Objects -> list of Domain Task Objects
     *
     * @param taskEntities list of JPA Task Objects
     *
     * @return list of Domain Task Objects
     */
    List<Task> TaskEntitiesToTasks(List<TaskEntity> taskEntities);

}
