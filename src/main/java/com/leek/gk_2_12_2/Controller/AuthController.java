package com.leek.gk_2_12_2.Controller;

import com.leek.gk_2_12_2.DTO.RegisterRequest;
import com.leek.gk_2_12_2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered. Please verify your email.");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String email, @RequestParam String otp) {
        boolean verified = userService.verifyOtp(email, otp);
        return verified ? ResponseEntity.ok("Account verified.") : ResponseEntity.badRequest().body("Invalid OTP.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) throws Exception {
        String token = userService.login(email, password);
        return ResponseEntity.ok(token);
    }
}
