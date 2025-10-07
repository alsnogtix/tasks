package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.model.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) {
        
        try {
            // 1. Authenticate the user using the AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authenticationRequest.username(), 
                    authenticationRequest.password()
                )
            );


            // 2. If authentication succeeds, get UserDetails to generate the token
            // We can safely cast the Authentication.getPrincipal() to UserDetails
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // 3. Generate the JWT token
            final String jwt = jwtUtil.generateToken(userDetails);

            // 4. Return the token in the response body
            return ResponseEntity.ok(new LoginResponse(userDetails.getUsername(), jwt));

        } catch (Exception e) {
            // If authentication fails (bad username/password)
            return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
    }
}
