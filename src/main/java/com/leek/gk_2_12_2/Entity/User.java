package com.leek.gk_2_12_2.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String fullName;
    private String password;
    private String phone;
    private String address;
    private String avatar;
    private LocalDateTime dateOfBirth;
    private LocalDateTime createdDate = LocalDateTime.now();
    private String role = "USER";
    private boolean isEnabled = false;
    private String otp; // OTP for email verification
    private LocalDateTime otpExpiryTime;

}
