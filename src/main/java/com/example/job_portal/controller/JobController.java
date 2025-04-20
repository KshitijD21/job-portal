package com.example.job_portal.controller;

import com.example.job_portal.entities.Job;
import com.example.job_portal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@CrossOrigin(origins = "http://localhost:3000") // adjust as needed
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job savedJob = jobService.saveJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable String id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}