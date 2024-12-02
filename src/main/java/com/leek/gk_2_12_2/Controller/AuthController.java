package com.leek.gk_2_12_2.Controller;

import com.leek.gk_2_12_2.DTO.RegisterRequest;
import com.leek.gk_2_12_2.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // Tạo cookie chứa JWT
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600) // Thời gian tồn tại 1 giờ
                .build();

        return ResponseEntity.ok().header("set-cookie", cookie.toString()).body("Login successful.");
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Xóa cookie bằng cách đặt Max-Age về 0
        ResponseCookie cookie = ResponseCookie.from("jwt", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // Đặt thời gian tồn tại bằng 0 để xóa cookie
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body("Logged out and cookie cleared.");
    }
}
