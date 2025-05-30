package com.example.job_portal.auth;


import com.example.job_portal.entities.UserPrinciple;
import com.example.job_portal.entities.Users;


import com.example.job_portal.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(email);
        if (user == null) {
            System.out.println("User not found for username: " + email);
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("User found: " + user);
        return new UserPrinciple(user);
    }


}
