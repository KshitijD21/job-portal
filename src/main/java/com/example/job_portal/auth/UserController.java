package com.example.job_portal.auth;

import com.example.job_portal.common.ApiResponse;
import com.example.job_portal.entities.LoginRequest;
import com.example.job_portal.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        System.out.println("user in register " + user);
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("loginRequest " + loginRequest);
        return userService.verify(loginRequest);
    }

    @GetMapping("/allUser")
    public List<Users> allUser() {
        return userService.getAllUsers();
    }

}
