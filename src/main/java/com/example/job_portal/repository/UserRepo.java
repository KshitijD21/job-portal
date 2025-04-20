package com.example.job_portal.repository;

import com.example.job_portal.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepo extends MongoRepository<Users, String> {

    @Query("{ 'Email' : ?0 }")
    Users findByEmail(String email);

    @Query("{ 'userName' : ?0 }")
    Users findByUserName(String userName);

}
