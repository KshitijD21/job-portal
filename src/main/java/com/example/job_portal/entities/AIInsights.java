package com.example.job_portal.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

// AIInsights.ts (MongoDB model)
@Document(collection = "ai_insights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIInsights {
    @Id
    private String id;
    private String userId;
    private String jobId;

    private String resumeText;
    private String jobDescription;

    private List<String> missingSkills;
    private List<String> improvementSuggestions;
    private String rawGeminiResponse;

    private Date generatedAt = new Date();
}
