package com.example.job_portal.common;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String buildPrompt(String rawText) {
        return """
        I am uploading a resume. Your task is to extract the following fields and return the result in **strictly valid JSON format only**. Do not include any additional text or explanation.

        Output JSON format:
        {
          "name": "",
          "email": "",
          "phone": "",
          "education": "",
          "summary": "",
          "skills": [],
          "domainScores": { 
            "softwareEngineering": 0,
            "uiUxDesign": 0,
            "dataAnalytics": 0,
            "fullStackDevelopment": 0,
            "cloudComputing": 0
          }
        }

        🔹 Notes:
        - "skills": List all relevant technical and soft skills mentioned in the resume.
        - "domainScores": Evaluate and assign a numeric score (1 to 10) based on the resume's content for each of the domains:
            • softwareEngineering: General development experience, coding proficiency, OOP, problem-solving.
            • uiUxDesign: UI/UX tools and design principles (e.g., Figma, Adobe, wireframing).
            • dataAnalytics: Tools or projects involving Excel, SQL, Python, Power BI, Tableau.
            • fullStackDevelopment: Experience across both frontend and backend development.
            • cloudComputing: Tools like AWS, Azure, GCP, DevOps, containerization (e.g., Docker, K8s).

        🚫 Do not invent or assume data. Use only what is present in the resume.

        Resume Text:
        """ + rawText;
    }
}
