package com.example.job_portal.service;

import com.example.job_portal.entities.Resume;
import com.example.job_portal.entities.UserPrinciple;
import com.example.job_portal.repository.ResumeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Resume parseAndSaveResume(String rawText,
                                     JsonNode geminiOutput) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            String userId = userPrinciple.getUser().getId();

            Resume resume = new Resume();
            resume.setName(geminiOutput.path("name").asText());
            resume.setUserId(userId);
            resume.setEmail(geminiOutput.path("email").asText());
            resume.setPhone(geminiOutput.path("phone").asText());
            resume.setEducation(geminiOutput.path("education").asText());
            resume.setExperience(geminiOutput.path("summary").asText()); // or rename if needed
            resume.setSkillsSummary(String.join(", ", objectMapper.convertValue(geminiOutput.path("skills"), List.class)));
            resume.setSkillScores(objectMapper.convertValue(geminiOutput.path("domainScores"), Map.class));
            resume.setRawText(rawText);

            return resumeRepository.save(resume);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini output", e);
        }
    }

    public Resume getLatestResumeByUserId(String userId) {
        return resumeRepository.findLatestByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No resume found for user " + userId));
    }


}
