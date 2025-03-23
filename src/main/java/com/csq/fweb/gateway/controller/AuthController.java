package com.csq.fweb.gateway.controller;

import com.csq.fweb.gateway.entity.User;
import com.csq.fweb.gateway.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("user", registeredUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        userService.findByUsername(user.getUsername())
               .filter(u -> new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(user.getPassword(), u.getPassword()))
               .ifPresent(u -> {
                    String token = userService.generateToken(u);
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Login successful");
                    response.put("token", token);
                    return ResponseEntity.ok(response);
                });
        return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestHeader("Authorization") String oldToken) {
        String newToken = userService.refreshToken(oldToken.replace("Bearer ", ""));
        if (newToken != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Token refreshed successfully");
            response.put("token", newToken);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(Map.of("message", "Invalid token"));
    }
}    