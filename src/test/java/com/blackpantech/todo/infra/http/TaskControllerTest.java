package com.blackpantech.todo.infra.http;

import com.blackpantech.todo.domain.task.Task;
import com.blackpantech.todo.domain.task.TaskService;
import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TaskService taskService;

    @Autowired
    ObjectMapper objectMapper;

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, 2025-08-23T22:00:00"
    })
    @DisplayName("should get a single task")
    void shouldGetTask(final long id, final String title, final boolean completed, final long order, final LocalDateTime dueDate) throws Exception {
        final Task task = new Task(id, title, completed, order, dueDate);
        when(taskService.getTask(id)).thenReturn(task);

        mockMvc.perform(get("/tasks/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(task), JsonCompareMode.STRICT));

        verify(taskService).getTask(id);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    @DisplayName("should return 404 when getting a single task")
    void shouldNotFindTask_whenGetTask() throws Exception {
        when(taskService.getTask(anyLong())).thenThrow(new TaskNotFoundException(0L));

        mockMvc.perform(get("/tasks/{id}", 0L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        verify(taskService).getTask(anyLong());
        verifyNoMoreInteractions(taskService);
    }

    @ParameterizedTest
    @CsvSource({
            "title, 2025-08-23T22:00:00",
            "title2, 2025-02-24T13:30:00"
    })
    @DisplayName("should create a task")
    void shouldCreateTask(final String title, final LocalDateTime dueDate) throws Exception {
        Task expectedTask = new Task(0L, title, false, 1L, dueDate);
        TaskToCreateRequest taskToCreateRequest = new TaskToCreateRequest(title, dueDate);
        when(taskService.createTask(title, dueDate)).thenReturn(expectedTask);

        mockMvc.perform(post("/tasks")
                        .content(objectMapper.writeValueAsString(taskToCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTask), JsonCompareMode.STRICT));

        verify(taskService).createTask(title, dueDate);
        verifyNoMoreInteractions(taskService);
    }

    @ParameterizedTest
    @CsvSource({
            "title, 2025-08-23T22:00:00",
            "title2, 2025-02-24T13:30:00"
    })
    @DisplayName("should return 409 when creating a task")
    void shouldFindDuplicatedTitle_whenCreateTask(final String title, final LocalDateTime dueDate) throws Exception {
        when(taskService.createTask(title, dueDate)).thenThrow(new DuplicatedTaskTitleException(title));
        TaskToCreateRequest taskToCreateRequest = new TaskToCreateRequest(title, dueDate);

        mockMvc.perform(post("/tasks")
                        .content(objectMapper.writeValueAsString(taskToCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());

        verify(taskService).createTask(title, dueDate);
        verifyNoMoreInteractions(taskService);
    }

    @ParameterizedTest
    @CsvSource({
            ", ",
            "   , 2025-02-24T13:30:00"
    })
    @DisplayName("should return 400 when creating a task")
    void shouldReturnBadRequest_whenCreateTask(final String title, final LocalDateTime dueDate) throws Exception {
        TaskToCreateRequest taskToCreateRequest = new TaskToCreateRequest(title, dueDate);

        mockMvc.perform(post("/tasks")
                        .content(objectMapper.writeValueAsString(taskToCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(taskService);
    }

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, ",
            "1, title2, true, 1, 2025-02-24T13:30:00"
    })
    @DisplayName("should edit a task")
    void shouldEditTask(final long id, final String title, final boolean completed, final long order, final LocalDateTime dueDate) throws Exception{
        Task expectedTask = new Task(id, title, completed, order, dueDate);
        TaskToEditRequest taskToEditRequest = new TaskToEditRequest(title, completed, order, dueDate);
        when(taskService.editTask(id, title, completed, order, dueDate)).thenReturn(expectedTask);

        mockMvc.perform(put("/tasks/{id}", id)
                        .content(objectMapper.writeValueAsString(taskToEditRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTask), JsonCompareMode.STRICT));

        verify(taskService).editTask(id, title, completed, order, dueDate);
        verifyNoMoreInteractions(taskService);
    }

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, ",
            "1, title2, true, 1, 2025-02-24T13:30:00"
    })
    @DisplayName("should return 409 when editing a task")
    void shouldFindDuplicatedTitle_whenEditTask(final long id, final String title, final boolean completed, final long order, final LocalDateTime dueDate) throws Exception {
        when(taskService.editTask(id, title, completed, order, dueDate)).thenThrow(new DuplicatedTaskTitleException(title));
        TaskToEditRequest taskToEditRequest = new TaskToEditRequest(title, completed, order, dueDate);

        mockMvc.perform(put("/tasks/{id}", id)
                        .content(objectMapper.writeValueAsString(taskToEditRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());

        verify(taskService).editTask(id, title, completed, order, dueDate);
        verifyNoMoreInteractions(taskService);
    }

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, ",
            "1, title2, true, 1, 2025-02-24T13:30:00"
    })
    @DisplayName("should return 404 when editing a task")
    void shouldReturnNotFound_whenEditTask(final long id, final String title, final boolean completed, final long order, final LocalDateTime dueDate) throws Exception {
        when(taskService.editTask(id, title, completed, order, dueDate)).thenThrow(new TaskNotFoundException(id));
        TaskToEditRequest taskToEditRequest = new TaskToEditRequest(title, completed, order, dueDate);

        mockMvc.perform(put("/tasks/{id}", id)
                        .content(objectMapper.writeValueAsString(taskToEditRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        verify(taskService).editTask(id, title, completed, order, dueDate);
        verifyNoMoreInteractions(taskService);
    }

    @ParameterizedTest
    @CsvSource({
            "0, , false, 1, ",
            "1,   , true, 1, 2025-02-24T13:30:00",
    })
    @DisplayName("should return 400 when editing a task")
    void shouldReturnBadRequest_whenEditTask(final long id, final String title, final boolean completed, final long order, final LocalDateTime dueDate) throws Exception {
        TaskToEditRequest taskToEditRequest = new TaskToEditRequest(title, completed, order, dueDate);

        mockMvc.perform(put("/tasks/{id}", id)
                        .content(objectMapper.writeValueAsString(taskToEditRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(taskService);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 3L, 5L})
    @DisplayName("should delete a single task")
    void shouldDeleteTask(final long id) throws Exception {
        mockMvc.perform(delete("/tasks/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(id);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    @DisplayName("should return 404 when deleting a task")
    void shouldNotFindTask_whenDeleteTask() throws Exception {
        doThrow(new TaskNotFoundException(0L)).when(taskService).deleteTask(anyLong());
        mockMvc.perform(delete("/tasks/{id}", 0L)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

        verify(taskService).deleteTask(anyLong());
        verifyNoMoreInteractions(taskService);
    }

    @ParameterizedTest
    @CsvSource({
            "0, title, false, 1, ",
            "1, title2, true, 1, 2025-02-24T13:30:00"
    })
    @DisplayName("should get all tasks")
    void shouldGetAllTasks(final long id, final String title, final boolean completed, final long order, final LocalDateTime dueDate) throws Exception {
        final List<Task> tasks = Collections.singletonList(new Task(id, title, completed, order, dueDate));
        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(tasks), JsonCompareMode.STRICT));

        verify(taskService).getAllTasks();
        verifyNoMoreInteractions(taskService);
    }

    @Test
    @DisplayName("should delete all tasks")
    void shouldDeleteAllTasks() throws Exception {
        mockMvc.perform(delete("/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        verify(taskService).deleteAllTasks();
        verifyNoMoreInteractions(taskService);
    }

    @Test
    @DisplayName("should delete all completed tasks")
    void shouldDeleteAllCompletedTasks() throws Exception {
        mockMvc.perform(delete("/tasks?completed=true")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());

        verify(taskService).deleteAllCompletedTasks();
        verifyNoMoreInteractions(taskService);
    }

}
