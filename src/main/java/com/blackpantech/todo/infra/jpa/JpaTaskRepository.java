package com.blackpantech.todo.infra.jpa;

import com.blackpantech.todo.domain.task.Task;
import com.blackpantech.todo.domain.task.TaskRepository;
import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of the task repository
 */
@Repository
public class JpaTaskRepository implements TaskRepository {

    private final TaskJpaRepository taskJpaRepository;

    private final TaskEntityMapper taskEntityMapper;

    public JpaTaskRepository(final TaskJpaRepository taskJpaRepository, final TaskEntityMapper taskEntityMapper) {
        this.taskJpaRepository = taskJpaRepository;
        this.taskEntityMapper = taskEntityMapper;
    }

    @Override
    public Task getTask(final long id) throws TaskNotFoundException {
        final TaskEntity task = getTaskById(id);

        return taskEntityMapper.TaskEntityToTask(task);
    }

    @Override
    public Task createTask(final String title, final LocalDateTime dueDate) throws DuplicatedTaskTitleException {
        final TaskEntity taskToCreate = getNewTaskWithUniqueTitle(title, dueDate);

        final TaskEntity createdTask = taskJpaRepository.save(taskToCreate);

        return taskEntityMapper.TaskEntityToTask(createdTask);
    }

    @Override
    public Task editTask(final long id,
                         final String title,
                         final boolean completed,
                         final long order,
                         final LocalDateTime dueDate)
            throws DuplicatedTaskTitleException, TaskNotFoundException {
        final TaskEntity taskToEdit = getTaskById(id);

        final TaskEntity editedTaskToSave = getEditedTaskWithUniqueTitle(taskToEdit, title, completed, order, dueDate);

        final TaskEntity editedTask = taskJpaRepository.save(editedTaskToSave);

        return taskEntityMapper.TaskEntityToTask(editedTask);
    }

    @Override
    public void deleteTask(final long id) throws TaskNotFoundException {
        taskJpaRepository.delete(getTaskById(id));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskEntityMapper.TaskEntitiesToTasks(taskJpaRepository.findAll());
    }

    @Override
    public void deleteAllTasks() {
        taskJpaRepository.deleteAll();
    }

    @Override
    public void deleteAllCompletedTasks() {
        taskJpaRepository.deleteAllCompletedTasks();
    }

    /**
     * Gets task with given ID
     *
     * @param id ID to look for
     *
     * @return task with given ID
     *
     * @throws TaskNotFoundException if no task was found
     */
    private TaskEntity getTaskById(final long id) throws TaskNotFoundException {
        final Optional<TaskEntity> optionalTaskEntity = taskJpaRepository.findById(id);

        if (optionalTaskEntity.isEmpty()) {
            throw new TaskNotFoundException(id);
        }

        return optionalTaskEntity.get();
    }

    /**
     * Gets a new task with a unique title
     *
     * @param title title of the new task
     * @param dueDate due date of the new task
     *
     * @return new task with given attributes
     *
     * @throws DuplicatedTaskTitleException if given title is already taken
     */
    private TaskEntity getNewTaskWithUniqueTitle(final String title, final LocalDateTime dueDate)
            throws DuplicatedTaskTitleException {
        final String uniqueTitle = getUniqueTitle(title);

        final long order = taskJpaRepository.count() + 1;

        return new TaskEntity(uniqueTitle, false, order, dueDate);
    }

    /**
     * Edits a given task with a unique title
     *
     * @param taskToEdit task to edit
     * @param title edited task title
     * @param completed edited task completion
     * @param order edited task position in list
     * @param dueDate edited due date
     *
     * @return edited task with a unique title
     *
     * @throws DuplicatedTaskTitleException if edited task title is not unique
     */
    private TaskEntity getEditedTaskWithUniqueTitle(final TaskEntity taskToEdit,
                                                    final String title,
                                                    final boolean completed,
                                                    final long order,
                                                    final LocalDateTime dueDate)
            throws DuplicatedTaskTitleException {
        if (!taskToEdit.getTitle().equals(title)) {
            return editGivenTask(taskToEdit, getUniqueTitle(title), completed, order, dueDate);
        } else {
            return editGivenTask(taskToEdit, title, completed, order, dueDate);
        }
    }

    /**
     * Edits a given task with given attributes
     *
     * @param taskToEdit task to edit
     * @param title edited task title
     * @param completed edited task completion
     * @param order edited task position in list
     * @param dueDate edited due date
     *
     * @return edited task
     */
    private TaskEntity editGivenTask(final TaskEntity taskToEdit,
                                     final String title,
                                     final boolean completed,
                                     final long order,
                                     final LocalDateTime dueDate) {
        taskToEdit.setTitle(title);
        taskToEdit.setCompleted(completed);
        taskToEdit.setOrderPosition(order);
        taskToEdit.setDueDate(dueDate);

        return taskToEdit;
    }

    /**
     * Gets unique title or throws exception if given title is not unique
     *
     * @param title given title
     *
     * @return unique task title
     *
     * @throws DuplicatedTaskTitleException if given title is not unique
     */
    private String getUniqueTitle(final String title) throws DuplicatedTaskTitleException {
        final Optional<TaskEntity> optionalTaskEntity = taskJpaRepository.findByTitle(title);

        if (optionalTaskEntity.isPresent()) {
            throw new DuplicatedTaskTitleException(title);
        }

        return title;
    }

}
