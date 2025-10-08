package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userRepository;

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public Task save(Task task){
        User user = getCurrentUser();
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Optional<Task> findById(Long id){
        return taskRepository.findById(id);
    }

    public Task update(Long id, Task taskDetails) {
        User user = getCurrentUser();
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if(!task.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not authorized to update this task");
        }

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.isStatus());

        return taskRepository.save(task);
    }

    public void delete(Long id) {
        User user = getCurrentUser();
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if(!task.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You are not authorized to delete this task");
        }

        taskRepository.delete(task);
    }


    // Method to get the current user
    private User getCurrentUser(){
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if(principal instanceof UserDetails){
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
    }

    public List<Task> findAllByUser(){
        User user = getCurrentUser();
        return taskRepository.findByUser(user);
    }

}
