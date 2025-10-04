package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        if(user.getUsername() == null || user.getPassword() == null){
            return new ResponseEntity<>("Username and password are required", HttpStatus.BAD_REQUEST);
        }

        if(userService.findByUsername(user.getUsername()).isPresent()){
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        User newUser = userService.registerNewUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

}
