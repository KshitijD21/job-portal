package com.example.job_portal.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "jobs2")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    private String id;

    private String jobId;
    private String company;
    private String companyProfile;  // Stored as raw JSON string or create a nested object if you prefer
    private int companySize;

    private String jobTitle;
    private String role;
    private String location;
    private String country;

    private double latitude;
    private double longitude;

    private String experience;
    private String qualifications;
    private String preference;

    private String jobDescription;
    private String responsibilities;
    private String benefits;

    private String salaryRange;
    private String workType;
    private String jobPostingDate;

    private List<String> skills;  // Expected to be split from comma or space-delimited string

    private String contactPerson;
    private String contact;

    private String jobPortal;
}