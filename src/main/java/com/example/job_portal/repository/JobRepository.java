package com.example.job_portal.repository;

import com.example.job_portal.entities.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    // Optional: add custom queries here
}
