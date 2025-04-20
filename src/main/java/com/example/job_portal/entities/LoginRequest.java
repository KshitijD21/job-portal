package com.example.job_portal.entities;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
