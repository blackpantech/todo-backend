package com.blackpantech.todo.domain.task;

import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain service to get, create, edit and delete tasks
 */
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
    public Task getTask(final long id) throws TaskNotFoundException {
        return taskRepository.getTask(id);
    }

    /**
     * Creates new task with given title and due date
     *
     * @param title title of the new task
     * @param dueDate due date of the new task
     *
     * @return created task
     *
     * @throws DuplicatedTaskTitleException if the given title is already taken
     */
    public Task createTask(final String title, final LocalDateTime dueDate) throws DuplicatedTaskTitleException {
        return taskRepository.createTask(title, dueDate);
    }

    /**
     * Edits task with given ID and new properties
     *
     * @param id ID to look for
     * @param title edited title
     * @param completed edited completion of the task
     * @param order edited position of the task in the list
     * @param dueDate edited due date
     *
     * @return edited task
     *
     * @throws DuplicatedTaskTitleException if the given title is already taken
     * @throws TaskNotFoundException if no task with given ID was found
     */
    public Task editTask(final long id,
                         final String title,
                         final boolean completed,
                         final long order,
                         final LocalDateTime dueDate)
            throws TaskNotFoundException, DuplicatedTaskTitleException {
        return taskRepository.editTask(id, title, completed, order, dueDate);
    }

    /**
     * Deletes a tasks with given ID
     *
     * @param id ID to look for
     *
     * @throws TaskNotFoundException if no task with given ID was found
     */
    public void deleteTask(final long id) throws TaskNotFoundException {
        taskRepository.deleteTask(id);
    }

    /**
     * Gets all tasks
     *
     * @return list of all tasks
     */
    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    /**
     * Deletes all tasks
     */
    public void deleteAllTasks() {
        taskRepository.deleteAllTasks();
    }

    /**
     * Deletes all completed tasks
     */
    public void deleteAllCompletedTasks() {
        taskRepository.deleteAllCompletedTasks();
    }

}
