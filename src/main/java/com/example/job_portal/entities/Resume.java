package com.example.job_portal.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document(collection = "resumes")
@Data
public class Resume {
    @Id
    private String id;
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String education;
    private String experience;
    private String skillsSummary;
    private Map<String, Integer> skillScores;
    private String rawText;
    private Date uploadedAt = new Date();

}

