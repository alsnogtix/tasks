package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Task;
import com.example.demo.service.TaskService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/tasks") 
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping 
    public List<Task> getAllTasks() {
        return taskService.findAllByUser();
    }

    @PostMapping 
    public Task createTask(@RequestBody Task task) {
        return taskService.save(task);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @PutMapping("/{id}")
    public Task updateTask(@RequestBody Task task, @PathVariable Long id) {
        return taskService.update(id, task);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
