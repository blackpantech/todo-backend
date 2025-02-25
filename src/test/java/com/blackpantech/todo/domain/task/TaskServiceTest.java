package com.blackpantech.todo.domain.task;

import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    @Mock
    final TaskRepository taskRepository = mock(TaskRepository.class);

    final TaskService taskService = new TaskService(taskRepository);

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
        final Task task = new Task(id, title, completed, order, dueDate);
        when(taskRepository.getTask(id)).thenReturn(task);

        final Task fetchedTask = taskService.getTask(id);

        assertEquals(task, fetchedTask);
        verify(taskRepository).getTask(id);
        verifyNoMoreInteractions(taskRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 3L, 5L})
    @DisplayName("should not find task when getting a single task")
    void shouldNotFindTask_whenGetTask(final long id) throws TaskNotFoundException {
        doThrow(new TaskNotFoundException(id)).when(taskRepository).getTask(id);

        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(id));

        verify(taskRepository).getTask(id);
        verifyNoMoreInteractions(taskRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "title, 2025-08-23T22:00:00",
            "title2, 2025-02-24T13:30:00"
    })
    @DisplayName("should create a task")
    void shouldCreateTask(final String title, final LocalDateTime dueDate) throws DuplicatedTaskTitleException {
        final Task task = new Task(0L, title, false, 1L, dueDate);
        when(taskRepository.createTask(title, dueDate)).thenReturn(task);

        final Task createdTask = taskService.createTask(title, dueDate);

        assertEquals(task, createdTask);
        verify(taskRepository).createTask(title, dueDate);
        verifyNoMoreInteractions(taskRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "title, 2025-08-23T22:00:00",
            "title2, 2025-02-24T13:30:00"
    })
    @DisplayName("should find task with the same title when creating a task")
    void shouldFindExistingTitle_whenCreateTask(final String title, final LocalDateTime dueDate)
            throws DuplicatedTaskTitleException {
        doThrow(new DuplicatedTaskTitleException(title)).when(taskRepository).createTask(title, dueDate);

        assertThrows(DuplicatedTaskTitleException.class, () -> taskService.createTask(title, dueDate));

        verify(taskRepository).createTask(title, dueDate);
        verifyNoMoreInteractions(taskRepository);
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
        final Task task = new Task(id, title, completed, order, dueDate);
        when(taskRepository.editTask(id, title, completed, order, dueDate)).thenReturn(task);

        final Task createdTask = taskService.editTask(id, title, completed, order, dueDate);

        assertEquals(task, createdTask);
        verify(taskRepository).editTask(id, title, completed, order, dueDate);
        verifyNoMoreInteractions(taskRepository);
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
                                              final LocalDateTime dueDate)
            throws DuplicatedTaskTitleException, TaskNotFoundException {
        doThrow(new DuplicatedTaskTitleException(title))
                .when(taskRepository).editTask(id, title, completed, order, dueDate);

        assertThrows(DuplicatedTaskTitleException.class,
                () -> taskService.editTask(id, title, completed, order, dueDate));

        verify(taskRepository).editTask(id, title, completed, order, dueDate);
        verifyNoMoreInteractions(taskRepository);
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
                                        final LocalDateTime dueDate)
            throws TaskNotFoundException, DuplicatedTaskTitleException {
        doThrow(new TaskNotFoundException(id)).when(taskRepository).editTask(id, title, completed, order, dueDate);

        assertThrows(TaskNotFoundException.class, () -> taskService.editTask(id, title, completed, order, dueDate));

        verify(taskRepository).editTask(id, title, completed, order, dueDate);
        verifyNoMoreInteractions(taskRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 3L, 5L})
    @DisplayName("should delete a single task")
    void shouldDeleteTask(final long id) throws TaskNotFoundException {
        taskService.deleteTask(id);

        verify(taskRepository).deleteTask(id);
        verifyNoMoreInteractions(taskRepository);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 3L, 5L})
    @DisplayName("should not find task when deleting a single task")
    void shouldNotFindTask_whenDeleteTask(final long id) throws TaskNotFoundException {
        doThrow(new TaskNotFoundException(id)).when(taskRepository).deleteTask(id);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(id));

        verify(taskRepository).deleteTask(id);
        verifyNoMoreInteractions(taskRepository);
    }

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, ",
            "1, title2, true, 1, 2025-02-24T13:30:00"
    })
    @DisplayName("should get all tasks")
    void shouldGetAllTasks(final long id,
                           final String title,
                           final boolean completed,
                           final long order,
                           final LocalDateTime dueDate) {
        final List<Task> tasks = Collections.singletonList(new Task(id, title, completed, order, dueDate));
        when(taskRepository.getAllTasks()).thenReturn(tasks);

        final List<Task> fetchedTasks = taskService.getAllTasks();

        assertFalse(fetchedTasks.isEmpty());
        assertTrue(fetchedTasks.containsAll(tasks));
        verify(taskRepository).getAllTasks();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    @DisplayName("should delete all tasks")
    void shouldDeleteAllTasks() {
        taskService.deleteAllTasks();

        verify(taskRepository).deleteAllTasks();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    @DisplayName("should delete all completed tasks")
    void shouldDeleteAllCompletedTasks() {
        taskService.deleteAllCompletedTasks();

        verify(taskRepository).deleteAllCompletedTasks();
        verifyNoMoreInteractions(taskRepository);
    }

}
