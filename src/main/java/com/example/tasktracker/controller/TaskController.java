package com.example.tasktracker.controller;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes task endpoints.
 *
 * @RestController  = @Controller + @ResponseBody → every method return value
 *                    is serialized to JSON automatically.
 * @RequestMapping  = sets the base URL prefix for all endpoints in this class.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Constructor injection — Spring automatically provides the TaskService bean.
     * This is the recommended way to inject dependencies (no @Autowired needed
     * when there is a single constructor).
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /api/tasks
     * Returns all tasks as a JSON array with HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * POST /api/tasks
     * Accepts a JSON body like {"title": "My task", "completed": false},
     * creates the task, and returns it with an assigned ID and HTTP 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task created = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
