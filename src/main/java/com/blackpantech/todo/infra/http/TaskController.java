package com.blackpantech.todo.infra.http;

import com.blackpantech.todo.domain.task.Task;
import com.blackpantech.todo.domain.task.TaskService;
import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") final long id) throws TaskNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.getTask(id));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskToCreateRequest taskToCreate)throws DuplicatedTaskTitleException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(taskToCreate.title(), taskToCreate.dueDate()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> editTask(@PathVariable("id") final long id,
                         @RequestBody @Valid final TaskToEditRequest taskToEdit)
            throws TaskNotFoundException, DuplicatedTaskTitleException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.editTask(id,
                        taskToEdit.title(),
                        taskToEdit.completed(),
                        taskToEdit.order(),
                        taskToEdit.dueDate()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("id") final long id) throws TaskNotFoundException {
        taskService.deleteTask(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllTasks(@RequestParam(required = false) final boolean completed) {
        if (completed) {
            taskService.deleteAllCompletedTasks();
        } else {
            taskService.deleteAllTasks();
        }
    }

    @ExceptionHandler(DuplicatedTaskTitleException.class)
    ResponseEntity<?> handleDuplicatedTaskTitleException() {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(TaskNotFoundException.class)
    ResponseEntity<?> handleTaskNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

}
