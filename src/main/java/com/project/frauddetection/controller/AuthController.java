package com.project.frauddetection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.project.frauddetection.model.User;
import com.project.frauddetection.repository.UserRepository;
import com.project.frauddetection.security.JwtUtil;

import io.jsonwebtoken.Claims;



@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists";
        }

       
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser == null) {
            return "User not found";
        }

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return "Invalid password";
        }

        //  Generate JWT token
        String token = JwtUtil.generateToken(
                existingUser.getUsername(),
                existingUser.getRole()
        );

        return token;
    }
    
    @GetMapping("/secure")
    public String secureEndpoint(
            @RequestHeader("Authorization") String token) {

        token = token.replace("Bearer ", "");

        Claims claims = JwtUtil.validateToken(token);

        return "Hello " + claims.getSubject()
                + ", Role: " + claims.get("role");
    }
    
}