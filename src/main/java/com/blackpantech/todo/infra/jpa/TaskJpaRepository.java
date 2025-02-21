package com.blackpantech.todo.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {

}
