package com.leek.gk_2_12_2.Service;

import com.leek.gk_2_12_2.DTO.RegisterRequest;
import com.leek.gk_2_12_2.Entity.User;
import com.leek.gk_2_12_2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        String otp = String.format("%06d", new Random().nextInt(999999));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setOtp(otp);
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        emailService.sendOtp(request.getEmail(), otp);

        return user;
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        if (user.getOtp().equals(otp) && user.getOtpExpiryTime().isAfter(LocalDateTime.now())) {
            user.setEnabled(true);
            user.setOtp(null);
            user.setOtpExpiryTime(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    public String login(String email, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty() || !passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(email);
    }
}
