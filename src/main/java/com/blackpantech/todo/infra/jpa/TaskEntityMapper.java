package com.blackpantech.todo.infra.jpa;

import com.blackpantech.todo.domain.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskEntityMapper {

    TaskEntityMapper INSTANCE = Mappers.getMapper(TaskEntityMapper.class);

    @Mapping(source = "taskEntity.orderPosition", target = "order")
    Task TaskEntityToTask(TaskEntity taskEntity);

    List<Task> TaskEntitiesToTasks(List<TaskEntity> taskEntity);

}
