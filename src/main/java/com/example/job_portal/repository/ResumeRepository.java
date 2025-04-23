package com.example.job_portal.repository;

import com.example.job_portal.entities.Resume;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends MongoRepository<Resume, String> {

    @Query(value = "{ 'userId': ?0 }", sort = "{ 'uploadedAt': -1 }")
    Optional<Resume> findLatestByUserId(String userId);
}