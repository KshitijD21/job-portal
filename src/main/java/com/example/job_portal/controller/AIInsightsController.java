package com.example.job_portal.controller;

import com.example.job_portal.entities.AIInsights;
import com.example.job_portal.entities.Job;
import com.example.job_portal.entities.Resume;
import com.example.job_portal.entities.UserPrinciple;
import com.example.job_portal.repository.AIInsightsRepository;
import com.example.job_portal.repository.JobRepository;
import com.example.job_portal.service.GeminiApiService;
import com.example.job_portal.service.JobService;
import com.example.job_portal.service.ResumeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ai")
public class AIInsightsController {

    @Autowired
    private ResumeService resumeService;
    @Autowired
    private JobService jobService;
    @Autowired
    private GeminiApiService geminiApiService;
    @Autowired
    private AIInsightsRepository insightsRepository;
    @Autowired
    private JobRepository jobRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/compare-resume")
    public ResponseEntity<AIInsights> analyze(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam String jobId
    ) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("Unauthorized: Cannot extract user");
        }

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        String userId = userPrinciple.getUser().getId();

        System.out.println("User id " + userId);

        Resume resume = resumeService.getLatestResumeByUserId(userId);
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        String prompt = buildComparisonPrompt(
                resume.getExperience(),
                resume.getSkillsSummary(),
                resume.getRawText(),
                job.getExperience(),
                job.getQualifications(),
                job.getPreference(),
                job.getJobDescription(),
                job.getResponsibilities()
        );

        String geminiResponseText = geminiApiService.sendTextToGemini(prompt);
        String cleanedResponse = geminiResponseText.trim();

        if (cleanedResponse.startsWith("```json")) {
            cleanedResponse = cleanedResponse.replace("```json", "").replace("```", "").trim();
        }

        JsonNode geminiResponse = objectMapper.readTree(cleanedResponse);

        AIInsights insights = new AIInsights(
                null,
                userId,
                jobId,
                resume.getRawText(),
                job.getJobDescription(),
                extractMissingSkills(geminiResponse),
                extractSuggestions(geminiResponse),
                geminiResponseText,
                new Date()
        );

        return ResponseEntity.ok(insightsRepository.save(insights));
    }

    public List<String> extractMissingSkills(JsonNode aiResponse) {
        List<String> missingSkills = new ArrayList<>();
        JsonNode skillsNode = aiResponse.path("missingSkills");
        if (skillsNode.isArray()) {
            for (JsonNode skill : skillsNode) {
                missingSkills.add(skill.asText());
            }
        }
        return missingSkills;
    }

    public List<String> extractSuggestions(JsonNode aiResponse) {
        List<String> suggestions = new ArrayList<>();
        JsonNode suggestionsNode = aiResponse.path("improvementSuggestions");
        if (suggestionsNode.isArray()) {
            for (JsonNode suggestion : suggestionsNode) {
                suggestions.add(suggestion.asText());
            }
        }
        return suggestions;
    }

    private String buildComparisonPrompt(String resumeExperience, String resumeSkills, String resumeRawText,
                                         String jobExperience, String jobQualifications, String jobPreference,
                                         String jobDescription, String jobResponsibilities) {

        return """
        You are a professional career assistant AI.
        Analyze the following resume and job description to:
        1. Identify any technical or soft skills missing in the resume when compared to the job.
        2. Provide 3 personalized, actionable suggestions to improve the resume based on candidate’s profile.

        Respond only in this strict JSON format:
        {
          "missingSkills": ["Skill A", "Skill B"],
          "improvementSuggestions": ["...", "..."]
        }

        === Resume ===
        Experience: %s
        Skills Summary: %s
        Full Resume Text: %s

        === Job Details ===
        Required Experience: %s
        Required Qualifications: %s
        Candidate Preference: %s
        Job Description: %s
        Responsibilities: %s
        """.formatted(
                resumeExperience,
                resumeSkills,
                resumeRawText,
                jobExperience,
                jobQualifications,
                jobPreference,
                jobDescription,
                jobResponsibilities
        );
    }
}
