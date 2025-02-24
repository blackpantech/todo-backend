package com.blackpantech.todo.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByTitle(final String title);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query("delete from TaskEntity task where task.completed = true")
    void deleteAllCompletedTasks();

}
