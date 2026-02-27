package com.etiya.etiyatelekom.api.controller;

import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisListResponse;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;
import com.etiya.etiyatelekom.business.abst.AIAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai-analysis")
@PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT','CUSTOMER')")
public class AIAnalysisController {

    private final AIAnalysisService aiAnalysisService;

    @GetMapping
    public ResponseEntity<AIAnalysisListResponse> getAll() {
        return ResponseEntity.ok(aiAnalysisService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<AIAnalysisListResponse> getActive() {
        return ResponseEntity.ok(aiAnalysisService.getActive());
    }

    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<AIAnalysisResponse> getByComplaintId(@PathVariable Long complaintId) {
        return ResponseEntity.ok(aiAnalysisService.getByComplaintId(complaintId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AIAnalysisResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(aiAnalysisService.getById(id));
    }
}
