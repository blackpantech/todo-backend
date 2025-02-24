package com.blackpantech.todo.infra.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "TASKS")
public class TaskEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "COMPLETED")
    private boolean completed;

    @Column(name = "ORDER_POSITION")
    private long orderPosition;

    @Column(name = "DUE_DATE")
    private LocalDateTime dueDate;

    public TaskEntity(String title, boolean completed, long orderPosition, LocalDateTime dueDate) {
        this.title = title;
        this.completed = completed;
        this.orderPosition = orderPosition;
        this.dueDate = dueDate;
    }

    protected TaskEntity() {
        // Does nothing
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public long getOrderPosition() {
        return orderPosition;
    }

    public void setOrderPosition(long orderPosition) {
        this.orderPosition = orderPosition;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

}
