package com.example.job_portal.repository;

import com.example.job_portal.entities.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends MongoRepository<Resume, String> {
}