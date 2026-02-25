package com.etiya.etiyatelekom.service.abst;


import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisListResponse;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;
import com.etiya.etiyatelekom.entity.AIAnalysis;
import com.etiya.etiyatelekom.entity.Complaint;

public interface AIAnalysisService {

    AIAnalysisResponse getById(Long id);

    AIAnalysisResponse getByComplaintId(Long complaintId);

    AIAnalysisListResponse getAll();

    AIAnalysisResponse create(Complaint complaint);

    AIAnalysis getEntityById(Long id);

}
