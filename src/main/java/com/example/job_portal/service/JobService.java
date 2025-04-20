package com.example.job_portal.service;

import com.example.job_portal.entities.Job;
import com.example.job_portal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public Optional<Job> getJobById(String id) {
        return jobRepository.findById(id);
    }
}
