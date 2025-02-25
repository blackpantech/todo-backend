package com.blackpantech.todo.infra.jpa;

import com.blackpantech.todo.domain.task.Task;
import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, 2025-08-23T22:00:00"
    })
    @DisplayName("should get a single task")
    void shouldGetTask(final long id,
                       final String title,
                       final boolean completed,
                       final long order,
                       final LocalDateTime dueDate)
            throws TaskNotFoundException {
        final TaskEntity task = new TaskEntity(title, completed, order, dueDate);
        when(taskJpaRepository.findById(id)).thenReturn(Optional.of(task));

        final Task fetchedTask = jpaTaskRepository.getTask(id);

        assertEquals(task.getTitle(), fetchedTask.title());
        assertEquals(task.isCompleted(), fetchedTask.completed());
        assertEquals(task.getOrderPosition(), fetchedTask.order());
        assertEquals(task.getDueDate(), fetchedTask.dueDate());
        verify(taskJpaRepository).findById(id);
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 3L, 5L})
    @DisplayName("should not find task when getting a single task")
    void shouldNotFindTask_whenGetTask(final long id) {
        when(taskJpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> jpaTaskRepository.getTask(id));

        verify(taskJpaRepository).findById(id);
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "title, 2025-08-23T22:00:00",
            "title2, 2025-02-24T13:30:00"
    })
    @DisplayName("should create a task")
    void shouldCreateTask(final String title, final LocalDateTime dueDate) throws DuplicatedTaskTitleException {
        final TaskEntity task = new TaskEntity(title, false, 1L, dueDate);
        when(taskJpaRepository.findByTitle(title)).thenReturn(Optional.empty());
        when(taskJpaRepository.count()).thenReturn(0L);
        when(taskJpaRepository.save(any())).thenReturn(task);

        final Task createdTask = jpaTaskRepository.createTask(title, dueDate);

        assertEquals(task.getTitle(), createdTask.title());
        assertEquals(task.isCompleted(), createdTask.completed());
        assertEquals(task.getOrderPosition(), createdTask.order());
        assertEquals(task.getDueDate(), createdTask.dueDate());
        verify(taskJpaRepository).findByTitle(title);
        verify(taskJpaRepository).count();
        verify(taskJpaRepository).save(any());
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "title, 2025-08-23T22:00:00",
            "title2, 2025-02-24T13:30:00"
    })
    @DisplayName("should find task with the same title when creating a task")
    void shouldFindExistingTitle_whenCreateTask(final String title, final LocalDateTime dueDate) {
        when(taskJpaRepository.findByTitle(title)).thenReturn(Optional.of(new TaskEntity()));

        assertThrows(DuplicatedTaskTitleException.class, () -> jpaTaskRepository.createTask(title, dueDate));

        verify(taskJpaRepository).findByTitle(title);
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, ",
            "1, title2, true, 1, 2025-02-24T13:30:00"
    })
    @DisplayName("should edit a task")
    void shouldEditTask(final long id,
                        final String title,
                        final boolean completed,
                        final long order,
                        final LocalDateTime dueDate)
            throws TaskNotFoundException, DuplicatedTaskTitleException {
        final TaskEntity task = new TaskEntity(title, completed, order, dueDate);
        final String editedTitle = title + " edit";
        when(taskJpaRepository.findByTitle(editedTitle)).thenReturn(Optional.empty());
        when(taskJpaRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskJpaRepository.save(any())).thenReturn(task);

        final Task editedTask = jpaTaskRepository.editTask(id, editedTitle, completed, order, dueDate);

        assertEquals(editedTitle, editedTask.title());
        assertEquals(task.isCompleted(), editedTask.completed());
        assertEquals(task.getOrderPosition(), editedTask.order());
        assertEquals(task.getDueDate(), editedTask.dueDate());
        verify(taskJpaRepository).findByTitle(editedTitle);
        verify(taskJpaRepository).findById(id);
        verify(taskJpaRepository).save(any());
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, ",
            "1, title2, true, 1, 2025-02-24T13:30:00"
    })
    @DisplayName("should edit a task but keep the same title")
    void shouldEditTask_withSameTitle(final long id,
                                      final String title,
                                      final boolean completed,
                                      final long order,
                                      final LocalDateTime dueDate)
            throws TaskNotFoundException, DuplicatedTaskTitleException {
        final TaskEntity task = new TaskEntity(title, completed, order, dueDate);
        when(taskJpaRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskJpaRepository.save(any())).thenReturn(task);

        final Task editedTask = jpaTaskRepository.editTask(id, title, !completed, order, dueDate);

        assertEquals(task.getTitle(), editedTask.title());
        assertEquals(task.isCompleted(), editedTask.completed());
        assertEquals(task.getOrderPosition(), editedTask.order());
        assertEquals(task.getDueDate(), editedTask.dueDate());
        verify(taskJpaRepository).findById(id);
        verify(taskJpaRepository).save(any());
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, ",
            "1, title2, true, 1, 2025-02-24T13:30:00"
    })
    @DisplayName("should find task with the same title when creating a task")
    void shouldFindExistingTitle_whenEditTask(final long id,
                                              final String title,
                                              final boolean completed,
                                              final long order,
                                              final LocalDateTime dueDate) {
        final TaskEntity task = new TaskEntity(title, completed, order, dueDate);
        final String editedTitle = title + " edit";
        when(taskJpaRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskJpaRepository.findByTitle(editedTitle)).thenReturn(Optional.of(new TaskEntity()));

        assertThrows(DuplicatedTaskTitleException.class,
                () -> jpaTaskRepository.editTask(id, editedTitle, completed, order, dueDate));

        verify(taskJpaRepository).findById(id);
        verify(taskJpaRepository).findByTitle(editedTitle);
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, ",
            "1, title2, true, 1, 2025-02-24T13:30:00"
    })
    @DisplayName("should not find task when editing a task")
    void shouldNotFindTask_whenEditTask(final long id,
                                        final String title,
                                        final boolean completed,
                                        final long order,
                                        final LocalDateTime dueDate) {
        when(taskJpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> jpaTaskRepository.editTask(id, title, completed, order, dueDate));

        verify(taskJpaRepository).findById(id);
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 3L, 5L})
    @DisplayName("should delete a single task")
    void shouldDeleteTask(final long id) throws TaskNotFoundException {
        final TaskEntity task = new TaskEntity();
        when(taskJpaRepository.findById(id)).thenReturn(Optional.of(task));

        jpaTaskRepository.deleteTask(id);

        verify(taskJpaRepository).findById(id);
        verify(taskJpaRepository).delete(any());
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 3L, 5L})
    @DisplayName("should not find task when getting a single task")
    void shouldNotFindTask_whenDeleteTask(final long id) {
        when(taskJpaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> jpaTaskRepository.deleteTask(id));

        verify(taskJpaRepository).findById(id);
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "title, false, 1, ",
            "title2, true, 1, 2025-02-24T13:30:00"
    })
    @DisplayName("should get all tasks")
    void shouldGetAllTasks(final String title, final boolean completed, final long order, final LocalDateTime dueDate) {
        final List<TaskEntity> tasks = Collections.singletonList(new TaskEntity(title, completed, order, dueDate));
        when(taskJpaRepository.findAll()).thenReturn(tasks);

        final List<Task> fetchedTasks = jpaTaskRepository.getAllTasks();

        assertEquals(tasks.size(), fetchedTasks.size());
        verify(taskJpaRepository).findAll();
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @Test
    @DisplayName("should delete all tasks")
    void shouldDeleteAllTasks() {
        jpaTaskRepository.deleteAllTasks();

        verify(taskJpaRepository).deleteAll();
        verifyNoMoreInteractions(taskJpaRepository);
    }

    @Test
    @DisplayName("should delete all completed tasks")
    void shouldDeleteAllCompletedTasks() {
        jpaTaskRepository.deleteAllCompletedTasks();

        verify(taskJpaRepository).deleteAllCompletedTasks();
        verifyNoMoreInteractions(taskJpaRepository);
    }

}
