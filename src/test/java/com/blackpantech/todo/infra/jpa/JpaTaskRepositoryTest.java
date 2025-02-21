package com.blackpantech.todo.infra.jpa;

import com.blackpantech.todo.domain.task.Task;
import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JpaTaskRepositoryTest {

    @MockitoBean
    TaskJpaRepository taskJpaRepository;

    @Autowired
    JpaTaskRepository jpaTaskRepository;

    final Task taskExample = new Task(0, "title", false, 1, null);

    final Task newTaskExample = new Task(0, "new title", false, 1, null);

    final Task editedTaskExample = new Task(0, "edited title", true, 1, null);

    final TaskEntity taskEntityExample = new TaskEntity("title", false, 1, null);

    final TaskEntity newTaskEntityExample = new TaskEntity("new title", false, 1, null);

    final TaskEntity editedTaskEntityExample = new TaskEntity("edited title", true, 1, null);

    @Test
    void shouldGetTask() throws TaskNotFoundException {
        when(taskJpaRepository.findById(0L)).thenReturn(Optional.of(taskEntityExample));

        final Task fetchedTask = jpaTaskRepository.getTask(0);

        assertEquals(taskExample, fetchedTask);
        verify(taskJpaRepository).findById(0L);
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @Test
    void shouldCreateTask() throws DuplicatedTaskTitleException {
        when(taskJpaRepository.findByTitle("new title")).thenReturn(Optional.empty());
        when(taskJpaRepository.count()).thenReturn(0L);
        when(taskJpaRepository.save(any())).thenReturn(newTaskEntityExample);

        final Task createdTask = jpaTaskRepository.createTask("new title", null);

        assertEquals(newTaskExample, createdTask);
        verify(taskJpaRepository).findByTitle("new title");
        verify(taskJpaRepository).count();
        verify(taskJpaRepository).save(any());
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @Test
    void shouldEditTask() throws TaskNotFoundException, DuplicatedTaskTitleException {
        when(taskJpaRepository.findByTitle("edited title")).thenReturn(Optional.empty());
        when(taskJpaRepository.findById(1L)).thenReturn(Optional.of(newTaskEntityExample));
        when(taskJpaRepository.save(any())).thenReturn(editedTaskEntityExample);

        final Task editedTask = jpaTaskRepository.editTask(1, "edited title", true, 2, null);

        assertEquals(editedTaskExample, editedTask);
        verify(taskJpaRepository).findByTitle("edited title");
        verify(taskJpaRepository).findById(1L);
        verify(taskJpaRepository).save(any());
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @Test
    void shouldDeleteTask() throws TaskNotFoundException {
        when(taskJpaRepository.findById(1L)).thenReturn(Optional.of(newTaskEntityExample));

        jpaTaskRepository.deleteTask(1);

        verify(taskJpaRepository).findById(1L);
        verify(taskJpaRepository).deleteById(1L);
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @Test
    void shouldGetAllTasks() {
        when(taskJpaRepository.findAll()).thenReturn(Arrays.asList(taskEntityExample, newTaskEntityExample));

        List<Task> fetchedTasks = jpaTaskRepository.getAllTasks();

        assertTrue(fetchedTasks.containsAll(Arrays.asList(taskExample, newTaskExample)));
        verify(taskJpaRepository).findAll();
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @Test
    void shouldDeleteAllTasks() {
        jpaTaskRepository.deleteAllTasks();

        verify(taskJpaRepository).deleteAll();
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @Test
    void shouldDeleteAllCompletedTasks() {
        jpaTaskRepository.deleteAllCompletedTasks();

        verify(taskJpaRepository).deleteAllCompletedTasks();
        verifyNoMoreInteractions(taskJpaRepository);
    }

}
