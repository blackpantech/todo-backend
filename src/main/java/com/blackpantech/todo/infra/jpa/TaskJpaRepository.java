package com.blackpantech.todo.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByTitle(final String title);

    @Query("delete from TaskEntity task where task.completed = true")
    void deleteAllCompletedTasks();

}
