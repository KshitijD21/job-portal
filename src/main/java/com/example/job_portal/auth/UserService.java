package com.example.job_portal.auth;


import com.example.job_portal.common.ApiResponse;
import com.example.job_portal.entities.LoginRequest;
import com.example.job_portal.entities.Users;
import com.example.job_portal.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepo repo;

    //comments

    private AuthenticationManager authenticationManager;

    @Autowired
    public UserService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    public Users register (Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Users savedUser =  repo.save(user);
        System.out.println("user register id: " + savedUser.getId());

        return savedUser;
    }

    public ResponseEntity<ApiResponse<Map<String, String>>> verify(LoginRequest users) {
        System.out.println("Inside verify");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(users.getEmail(), users.getPassword())
            );

            System.out.println("authentication.isAuthenticated(): " + authentication.isAuthenticated());

            if (authentication.isAuthenticated()) {
                Users authenticatedUser = repo.findByEmail(users.getEmail());
                if (authenticatedUser != null) {
                    System.out.println("Authenticated user: " + authenticatedUser);
                    String token = jwtService.generateToken(users.getEmail(), authenticatedUser.getId());
                    String role = authenticatedUser.getRole().name(); // get role from DB
                    Map<String, String> responseData = new HashMap<>();
                    responseData.put("token", token);
                    responseData.put("role", role);

                    ApiResponse<Map<String, String>> response = new ApiResponse<>("success", "Login successful", responseData, null);
                    System.out.println("response value is "+ response);
                    return ResponseEntity.ok(response);
                } else {
                    ApiResponse<Map<String, String>> response = new ApiResponse<>("error", "User not found", null, null);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } else {
                ApiResponse<Map<String, String>>  response = new ApiResponse<>("error", "Authentication failed", null, null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            ApiResponse<Map<String, String>> response = new ApiResponse<>("error", "Invalid email or password", null, null);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    public List<Users> getAllUsers() {
        System.out.println(" inside get all users " );
        List<Users> allData  = repo.findAll();
        System.out.println(" allData " + allData);
        return allData;

    }
}
