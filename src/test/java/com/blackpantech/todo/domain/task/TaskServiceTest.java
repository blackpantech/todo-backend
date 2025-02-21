package com.blackpantech.todo.domain.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    TaskService taskService = new TaskService(taskRepository);

    Task taskExample = new Task(1, "title sample", false, 1,"my.url.com/1", LocalDateTime.now());
    Task newTaskExample = new Task(2, "new title", false, 2,"my.url.com/2", null);
    Task editedTaskExample = new Task(2, "edited title", true, 2,"my.url.com/2", null);

    @Test
    void shouldGetTask() {
        Mockito.when(taskRepository.getTask(1)).thenReturn(taskExample);

        Task fetchedTask = taskService.getTask(1);

        assertEquals(taskExample, fetchedTask);
        verify(taskRepository).getTask(1);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldCreateTask() {
        Mockito.when(taskRepository.createTask("new title", null)).thenReturn(newTaskExample);

        Task createdTask = taskService.createTask("new title", null);

        assertEquals(newTaskExample, createdTask);
        verify(taskRepository).createTask("new title", null);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldEditTask() {
        Mockito.when(taskRepository.editTask(2, "edited title", true, 2, null)).thenReturn(editedTaskExample);

        Task editedTask = taskService.editTask(2, "edited title", true, 2, null);

        assertEquals(editedTaskExample, editedTask);
        verify(taskRepository).editTask(2, "edited title", true, 2, null);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldDeleteTask() {
        Mockito.when(taskRepository.deleteTask(2)).thenReturn(true);

        boolean isTaskDeleted = taskService.deleteTask(2);

        assertTrue(isTaskDeleted);
        verify(taskRepository).deleteTask(2);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldGetAllTasks() {
        Mockito.when(taskRepository.getAllTasks()).thenReturn(Arrays.asList(taskExample, newTaskExample));

        List<Task> fetchedTasks = taskService.getAllTasks();

        assertFalse(fetchedTasks.isEmpty());
        assertTrue(fetchedTasks.containsAll(Arrays.asList(taskExample, newTaskExample)));
        verify(taskRepository).getAllTasks();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldDeleteAllTasks() {
        Mockito.when(taskRepository.deleteAllTasks()).thenReturn(true);

        boolean areTasksDeleted = taskService.deleteAllTasks();

        assertTrue(areTasksDeleted);
        verify(taskRepository).deleteAllTasks();
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldDeleteAllCompletedTasks() {
        Mockito.when(taskRepository.deleteAllCompletedTasks()).thenReturn(true);

        boolean areCompletedTasksDeleted = taskService.deleteAllCompletedTasks();

        assertTrue(areCompletedTasksDeleted);
        verify(taskRepository).deleteAllCompletedTasks();
        verifyNoMoreInteractions(taskRepository);
    }

}
