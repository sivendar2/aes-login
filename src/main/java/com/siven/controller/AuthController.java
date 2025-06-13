package com.siven.controller;

import com.siven.entity.User;
import com.siven.repo.UserRepository;
import com.siven.utils.AESUtil;
import com.siven.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            // Encrypt email here, outside the entity
            String encryptedEmail = AESUtil.encrypt(user.getEmail());
            user.setEmail(encryptedEmail);

            // Hash password using BCrypt
            String hashedPassword = encoder.encode(user.getPassword());
            user.setPassword(hashedPassword);

            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Encryption/Registration failed: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser != null && encoder.matches(user.getPassword(), dbUser.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

}

