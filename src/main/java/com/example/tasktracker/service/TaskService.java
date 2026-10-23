package com.example.tasktracker.service;

import com.example.tasktracker.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service layer that manages tasks in memory.
 *
 * @Service tells Spring to register this class as a singleton bean so it can
 * be injected into controllers. All task data lives in a synchronized list
 * and is lost when the application restarts — this keeps things simple for
 * learning purposes.
 */
@Service
public class TaskService {

    /**
     * Thread-safe counter that generates unique IDs.
     * AtomicLong guarantees no two concurrent requests get the same ID.
     */
    private final AtomicLong idCounter = new AtomicLong(1);

    /**
     * In-memory store. We wrap an ArrayList with Collections.synchronizedList
     * so basic read/write operations are thread-safe.
     */
    private final List<Task> tasks = Collections.synchronizedList(new ArrayList<>());

    /**
     * Returns an unmodifiable snapshot of all tasks.
     */
    public List<Task> getAllTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Assigns an auto-generated ID to the incoming task, stores it, and
     * returns the saved instance.
     */
    public Task createTask(Task task) {
        task.setId(idCounter.getAndIncrement());
        tasks.add(task);
        return task;
    }
}
