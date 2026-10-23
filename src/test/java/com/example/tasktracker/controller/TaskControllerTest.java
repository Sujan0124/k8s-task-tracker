package com.example.tasktracker.controller;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TaskController.
 *
 * @WebMvcTest loads only the web layer (controllers, filters, etc.) — no full
 * application context — so these tests are fast.
 *
 * @MockBean replaces the real TaskService with a Mockito mock so we can
 * control its return values and verify interactions in isolation.
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;           // Simulates HTTP requests without starting a server

    @MockBean
    private TaskService taskService;   // Mocked service injected into the controller

    @Autowired
    private ObjectMapper objectMapper; // Jackson mapper for converting objects ↔ JSON

    // ------------------------------------------------------------------ GET

    @Test
    @DisplayName("GET /api/tasks → 200 OK with empty list when no tasks exist")
    void getAllTasks_empty() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/tasks → 200 OK with task list")
    void getAllTasks_withTasks() throws Exception {
        Task task1 = new Task(1L, "Write unit tests", false);
        Task task2 = new Task(2L, "Deploy to K8s", true);

        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Write unit tests"))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Deploy to K8s"))
                .andExpect(jsonPath("$[1].completed").value(true));
    }

    // ------------------------------------------------------------------ POST

    @Test
    @DisplayName("POST /api/tasks → 201 CREATED with assigned ID")
    void createTask_success() throws Exception {
        Task input = new Task(null, "Build Docker image", false);
        Task saved = new Task(1L, "Build Docker image", false);

        when(taskService.createTask(any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Build Docker image"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @DisplayName("POST /api/tasks → 201 CREATED with completed=true")
    void createTask_completed() throws Exception {
        Task input = new Task(null, "Review PR", true);
        Task saved = new Task(2L, "Review PR", true);

        when(taskService.createTask(any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Review PR"))
                .andExpect(jsonPath("$.completed").value(true));
    }
}
