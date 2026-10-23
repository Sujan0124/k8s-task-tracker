package com.example.tasktracker.service;

import com.example.tasktracker.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TaskService — pure Java tests with no Spring context needed.
 */
class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    @DisplayName("getAllTasks returns empty list initially")
    void getAllTasks_initiallyEmpty() {
        List<Task> tasks = taskService.getAllTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    @DisplayName("createTask assigns an ID and stores the task")
    void createTask_assignsId() {
        Task task = new Task();
        task.setTitle("Learn Spring Boot");
        task.setCompleted(false);

        Task created = taskService.createTask(task);

        assertNotNull(created.getId());
        assertEquals(1L, created.getId());
        assertEquals("Learn Spring Boot", created.getTitle());
        assertFalse(created.isCompleted());
    }

    @Test
    @DisplayName("Multiple tasks get sequential IDs")
    void createTask_sequentialIds() {
        Task t1 = new Task();
        t1.setTitle("First");
        Task t2 = new Task();
        t2.setTitle("Second");

        taskService.createTask(t1);
        taskService.createTask(t2);

        List<Task> all = taskService.getAllTasks();
        assertEquals(2, all.size());
        assertEquals(1L, all.get(0).getId());
        assertEquals(2L, all.get(1).getId());
    }

    @Test
    @DisplayName("getAllTasks returns an unmodifiable copy")
    void getAllTasks_returnsImmutableCopy() {
        Task task = new Task();
        task.setTitle("Test");
        taskService.createTask(task);

        List<Task> tasks = taskService.getAllTasks();
        assertThrows(UnsupportedOperationException.class, () -> tasks.add(new Task()));
    }
}
