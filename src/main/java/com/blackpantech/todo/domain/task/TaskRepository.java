package com.blackpantech.todo.domain.task;

import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain repository to interact with application repository
 */
public interface TaskRepository {

    /**
     * Gets task with given ID
     *
     * @param id ID to look for
     *
     * @return task with given ID
     *
     * @throws TaskNotFoundException if no task with given ID was found
     */
    Task getTask(final long id) throws TaskNotFoundException;

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
    Task createTask(final String title, final LocalDateTime dueDate) throws DuplicatedTaskTitleException;

    /**
     * Edits task with given ID with new properties
     *
     * @param id ID to look for
     * @param title edited title
     * @param done edited completion of the task
     * @param order edited position of the task in the list
     * @param dueDate edited due date
     *
     * @return edited task
     *
     * @throws DuplicatedTaskTitleException if the given title is already taken
     * @throws TaskNotFoundException if no task with given ID was found
     */
    Task editTask(final long id, final String title, final boolean done, final long order, final LocalDateTime dueDate)
            throws DuplicatedTaskTitleException, TaskNotFoundException;

    /**
     * Deletes a tasks with given ID
     *
     * @param id ID to look for
     *
     * @throws TaskNotFoundException if no task with given ID was found
     */
    void deleteTask(final long id) throws TaskNotFoundException;

    /**
     * Gets all the tasks
     *
     * @return list of all tasks
     */
    List<Task> getAllTasks();

    /**
     * Deletes all tasks
     */
    void deleteAllTasks();

    /**
     * Deletes all completed tasks
     */
    void deleteAllCompletedTasks();

}
