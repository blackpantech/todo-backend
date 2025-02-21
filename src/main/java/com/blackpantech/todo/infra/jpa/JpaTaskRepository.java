package com.blackpantech.todo.infra.jpa;

import com.blackpantech.todo.domain.task.Task;
import com.blackpantech.todo.domain.task.TaskRepository;
import com.blackpantech.todo.domain.task.exceptions.DuplicatedTaskTitleException;
import com.blackpantech.todo.domain.task.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        TaskEntity task = findTaskById(id);

        return taskEntityMapper.TaskEntityToTask(task);
    }

    @Override
    public Task createTask(final String title, final LocalDateTime dueDate) throws DuplicatedTaskTitleException {
        final TaskEntity taskToCreate = getTaskWithUniqueTitle(title, dueDate);

        final TaskEntity createdTask = taskJpaRepository.save(taskToCreate);

        return taskEntityMapper.TaskEntityToTask(createdTask);
    }

    @Override
    public Task editTask(final long id, final String title, final boolean done, final long order, final LocalDateTime dueDate)
            throws DuplicatedTaskTitleException, TaskNotFoundException {
        final TaskEntity taskToEdit = findTaskById(id);

        final TaskEntity editedTask = taskJpaRepository.save(getTaskWithUniqueTitle(taskToEdit, title, done, order, dueDate));

        return taskEntityMapper.TaskEntityToTask(editedTask);
    }

    @Override
    public void deleteTask(final long id) throws TaskNotFoundException {
        if (taskJpaRepository.findById(id).isEmpty()) {
            throw new TaskNotFoundException(id);
        }

        taskJpaRepository.deleteById(id);
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

    private TaskEntity findTaskById(final long id) throws TaskNotFoundException {
        final Optional<TaskEntity> optionalTaskEntity = taskJpaRepository.findById(id);

        if (optionalTaskEntity.isEmpty()) {
            throw new TaskNotFoundException(id);
        }

        return optionalTaskEntity.get();
    }

    private TaskEntity getTaskWithUniqueTitle(final String title, final LocalDateTime dueDate) throws DuplicatedTaskTitleException {
        final Optional<TaskEntity> optionalTaskEntity = taskJpaRepository.findByTitle(title);

        if (optionalTaskEntity.isPresent()) {
            throw new DuplicatedTaskTitleException(title);
        }

        return new TaskEntity(title, false, taskJpaRepository.count() + 1, dueDate);
    }

    private TaskEntity getTaskWithUniqueTitle(final TaskEntity taskToEdit, final String title, final boolean done, final long order, final LocalDateTime dueDate) throws DuplicatedTaskTitleException {
        final Optional<TaskEntity> optionalTaskEntity = taskJpaRepository.findByTitle(title);

        if (optionalTaskEntity.isPresent() && !optionalTaskEntity.get().getTitle().equals(title)) {
            throw new DuplicatedTaskTitleException(title);
        }

        taskToEdit.setTitle(title);
        taskToEdit.setDone(done);
        taskToEdit.setOrderPosition(order);
        taskToEdit.setDueDate(dueDate);

        return taskToEdit;
    }

}
