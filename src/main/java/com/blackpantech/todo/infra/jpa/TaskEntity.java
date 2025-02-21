package com.blackpantech.todo.infra.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class TaskEntity {

    @Id
    @GeneratedValue
    private long id;

    private String title;

    private boolean done;

    private long orderPosition;

    private String url;

    private LocalDateTime dueDate;

    public TaskEntity(String title, boolean done, long orderPosition, String url, LocalDateTime dueDate) {
        this.title = title;
        this.done = done;
        this.orderPosition = orderPosition;
        this.url = url;
        this.dueDate = dueDate;
    }

    protected TaskEntity() {
        // Does nothing
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public String getUrl() {
        return url;
    }

    public long getOrderPosition() {
        return orderPosition;
    }

    public boolean isDone() {
        return done;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

}
