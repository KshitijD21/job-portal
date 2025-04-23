package com.example.job_portal.repository;

import com.example.job_portal.entities.AIInsights;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIInsightsRepository extends MongoRepository<AIInsights, String> {}
