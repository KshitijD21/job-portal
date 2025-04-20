package com.example.job_portal.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    private String id;

    private String jobId;
    private String company;
    private String jobTitle;
    private String role;
    private String location;
    private String experience;
    private String salaryRange;
    private String workType;
    private String jobPostingDate;
    private List<String> skills; // stored as tags
}