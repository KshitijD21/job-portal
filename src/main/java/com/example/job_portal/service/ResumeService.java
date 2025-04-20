package com.example.job_portal.service;

import com.example.job_portal.entities.Resume;
import com.example.job_portal.repository.ResumeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

            Resume resume = new Resume();
            resume.setName(geminiOutput.path("name").asText());
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

}
