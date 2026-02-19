package com.etiya.etiyatelekom.service.abst;


import com.etiya.etiyatelekom.api.dto.request.aiAnalysisRequest.AIAnalysisUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisListResponse;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;

public interface AIAnalysisService {

    AIAnalysisResponse getById(Long id);

    AIAnalysisResponse getByComplaintId(Long complaintId);

    AIAnalysisListResponse getAll();

    AIAnalysisResponse create(Long complaintId);
}
