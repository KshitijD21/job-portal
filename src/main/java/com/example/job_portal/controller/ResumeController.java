package com.example.job_portal.controller;

import com.example.job_portal.common.PromptBuilder;
import com.example.job_portal.entities.Resume;
import com.example.job_portal.service.GeminiApiService;
import com.example.job_portal.service.PdfExtractorService;
import com.example.job_portal.service.ResumeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ResumeController {


    private final PdfExtractorService pdfExtractorService;
    private final GeminiApiService geminiApiService;
    private final ResumeService resumeService;
    private final PromptBuilder promptBuilder;

    @PostMapping("/resume/upload")
    public ResponseEntity<String> uploadResume(@RequestParam("resume") MultipartFile file) throws IOException {
        String uploadsDir = System.getProperty("user.dir") + "/uploads"; // absolute path
        File uploadDir = new File(uploadsDir);

        // ✅ Create uploads directory if not exists
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filePath = uploadsDir + "/" + file.getOriginalFilename();
        file.transferTo(new File(filePath)); // ✅ Now this won't fail

        String rawText = pdfExtractorService.extractTextFromPdf(filePath);
        System.out.println("raw text " + rawText );
        String aiSummary = geminiApiService.sendTextToGemini(promptBuilder.buildPrompt(rawText));
        System.out.println("aiSummary " + aiSummary );
        String cleanedJson = aiSummary.trim();
        if (cleanedJson.startsWith("```json")) {
            cleanedJson = cleanedJson.replaceAll("```json", "").replaceAll("```", "").trim();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(cleanedJson);

        Resume resume = resumeService.parseAndSaveResume(rawText, jsonNode);
        System.out.println("resume " + resume );

        return ResponseEntity.ok("Resume saved with ID: " + resume.getId());
    }

}
