package com.example.job_portal.payloads; // or whatever package you use

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class loginResponse {
    private String token;
    private String role;
}
