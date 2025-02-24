package com.blackpantech.todo.infra.jpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@DataJpaTest
public class TaskJpaRepositoryTest {

    @Autowired
    TaskJpaRepository taskJpaRepository;

    @Test
    @DisplayName("should persist and find new tasks")
    void shouldPersistNewData() {
        TaskEntity taskEntity = new TaskEntity("title", false, 1, LocalDateTime.now());

        taskJpaRepository.save(taskEntity);

        assertThat(taskJpaRepository.count()).isEqualTo(1);
        assertThat(taskJpaRepository.findByTitle("title"))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(taskEntity);
    }

    @Test
    @DisplayName("should delete all completed tasks")
    void shouldDeleteAllCompletedTasks() {
        TaskEntity taskEntity1 = new TaskEntity("title 1", false, 1, LocalDateTime.now());
        taskJpaRepository.save(taskEntity1);
        TaskEntity taskEntity2 = new TaskEntity("title 2", true, 2, LocalDateTime.now());
        taskJpaRepository.save(taskEntity2);
        TaskEntity taskEntity3 = new TaskEntity("title 3", true, 3, LocalDateTime.now());
        taskJpaRepository.save(taskEntity3);

        taskJpaRepository.deleteAllCompletedTasks();

        assertThat(taskJpaRepository.count()).isEqualTo(1);
        assertThat(taskJpaRepository.findByTitle("title 1"))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(taskEntity1);
    }

    @Test
    @DisplayName("should edit existing task")
    void shouldEditExistingTask() {
        TaskEntity taskEntity = new TaskEntity("title", false, 1, LocalDateTime.now());
        taskJpaRepository.save(taskEntity);

        Optional<TaskEntity> optionalTaskToEdit = taskJpaRepository.findByTitle("title");

        if (optionalTaskToEdit.isEmpty()) {
            fail();
        }

        TaskEntity taskToEdit = optionalTaskToEdit.get();
        taskToEdit.setTitle("edited title");

        assertThat(taskJpaRepository.count()).isEqualTo(1);
        assertThat(taskJpaRepository.findByTitle("edited title"))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(taskEntity);
    }

}
