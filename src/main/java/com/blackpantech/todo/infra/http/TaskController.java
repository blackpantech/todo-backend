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

/**
 * HTTP REST controller to interact with tasks
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Gets task with given ID
     *
     * @param id ID to look for
     *
     * @return task with given ID
     *
     * @throws TaskNotFoundException if no task with given ID was found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable("id") final long id) throws TaskNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.getTask(id));
    }

    /**
     * Creates new task with given title and due date
     *
     * @param taskToCreate new task properties
     *
     * @return created task
     *
     * @throws DuplicatedTaskTitleException if the given title is already taken
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskToCreateRequest taskToCreate)throws DuplicatedTaskTitleException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(taskToCreate.title(), taskToCreate.dueDate()));
    }

    /**
     * Edits task with given ID and new properties
     *
     * @param id ID to look for
     * @param taskToEdit edited task properties
     *
     * @return edited task
     *
     * @throws DuplicatedTaskTitleException if the given title is already taken
     * @throws TaskNotFoundException if no task with given ID was found
     */
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

    /**
     * Deletes a tasks with given ID
     *
     * @param id ID to look for
     *
     * @throws TaskNotFoundException if no task with given ID was found
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("id") final long id) throws TaskNotFoundException {
        taskService.deleteTask(id);
    }

    /**
     * Gets all tasks
     *
     * @return list of all tasks
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    /**
     * Deletes all tasks or all completed tasks
     *
     * @param completed optional request parameter to select completed tasks
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllTasks(@RequestParam(required = false) final boolean completed) {
        if (completed) {
            taskService.deleteAllCompletedTasks();
        } else {
            taskService.deleteAllTasks();
        }
    }

    /**
     * Exception handler for DuplicatedTaskTitleException
     *
     * @return 409 Conflict status
     */
    @ExceptionHandler(DuplicatedTaskTitleException.class)
    ResponseEntity<?> handleDuplicatedTaskTitleException() {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .build();
    }

    /**
     * Exception handler for TaskNotFoundException
     *
     * @return 404 Not Found status
     */
    @ExceptionHandler(TaskNotFoundException.class)
    ResponseEntity<?> handleTaskNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

}
