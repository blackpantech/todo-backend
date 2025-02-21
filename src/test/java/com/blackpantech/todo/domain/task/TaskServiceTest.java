package com.blackpantech.todo.domain.task;

import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    @Mock
    final TaskRepository taskRepository = mock(TaskRepository.class);

    final TaskService taskService = new TaskService(taskRepository);

    final Task taskExample = new Task(1, "title sample", false, 1, LocalDateTime.now());

    final Task newTaskExample = new Task(2, "new title", false, 2, null);

    final Task editedTaskExample = new Task(2, "edited title", true, 2, null);

    @Test
    void shouldGetTask() throws TaskNotFoundException {
        when(taskRepository.getTask(1)).thenReturn(taskExample);

        final Task fetchedTask = taskService.getTask(1);

        assertEquals(taskExample, fetchedTask);
        verify(taskRepository).getTask(1);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldCreateTask() throws DuplicatedTaskTitleException {
        when(taskRepository.createTask("new title", null)).thenReturn(newTaskExample);

        final Task createdTask = taskService.createTask("new title", null);

        assertEquals(newTaskExample, createdTask);
        verify(taskRepository).createTask("new title", null);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldEditTask() throws TaskNotFoundException, DuplicatedTaskTitleException {
        when(taskRepository.editTask(2, "edited title", true, 2, null)).thenReturn(editedTaskExample);

        final Task editedTask = taskService.editTask(2, "edited title", true, 2, null);

        assertEquals(editedTaskExample, editedTask);
        verify(taskRepository).editTask(2, "edited title", true, 2, null);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldDeleteTask() throws TaskNotFoundException {
        taskService.deleteTask(2);

        verify(taskRepository).deleteTask(2);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldGetAllTasks() {
        when(taskRepository.getAllTasks()).thenReturn(Arrays.asList(taskExample, newTaskExample));

        final List<Task> fetchedTasks = taskService.getAllTasks();

        assertFalse(fetchedTasks.isEmpty());
        assertTrue(fetchedTasks.containsAll(Arrays.asList(taskExample, newTaskExample)));
        verify(taskRepository).getAllTasks();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldDeleteAllTasks() {
        taskService.deleteAllTasks();

        verify(taskRepository).deleteAllTasks();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldDeleteAllCompletedTasks() {
        taskService.deleteAllCompletedTasks();

        verify(taskRepository).deleteAllCompletedTasks();
        verifyNoMoreInteractions(taskRepository);
    }

}
