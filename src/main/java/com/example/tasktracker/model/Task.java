package com.example.tasktracker.model;

/**
 * Task model — represents a single task in the tracker.
 *
 * This is a simple POJO (Plain Old Java Object) with three fields.
 * No database annotations are needed because we store tasks in memory.
 */
public class Task {

    /** Unique identifier, auto-assigned by the service layer. */
    private Long id;

    /** Short description of the task (e.g., "Deploy to staging"). */
    private String title;

    /** Whether the task has been finished. Defaults to false. */
    private boolean completed;

    // ---- Constructors ----

    /** No-arg constructor required by Jackson for JSON deserialization. */
    public Task() {
    }

    public Task(Long id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    // ---- Getters & Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
