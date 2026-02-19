package com.etiya.etiyatelekom.service.impl;

import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisListResponse;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.AIAnalysis;
import com.etiya.etiyatelekom.entity.Complaint;
import com.etiya.etiyatelekom.repository.AIAnalysisRepository;
import com.etiya.etiyatelekom.repository.ComplaintRepository;
import com.etiya.etiyatelekom.service.abst.AIAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AIAnalysisServiceImpl implements AIAnalysisService {

    private final AIAnalysisRepository aiAnalysisRepository;
    private final ModelMapperService modelMapperService;
    private final ComplaintRepository complaintRepository;

    @Override
    public AIAnalysisResponse getById(Long id) {
        AIAnalysis aiAnalysis=aiAnalysisRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("AIAnalysis","Id",id));

        AIAnalysisResponse aiAnalysisResponse=modelMapperService.forResponse().map(aiAnalysis,AIAnalysisResponse.class);

        return aiAnalysisResponse;
    }

    @Override
    public AIAnalysisResponse getByComplaintId(Long complaintId) {
        if (!aiAnalysisRepository.existsByComplaintId(complaintId)){
            throw new ResourceNotFoundException("AIAnalysis","Complaint",complaintId);
        }
        AIAnalysis aiAnalysis=aiAnalysisRepository.findByComplaintId(complaintId);
        AIAnalysisResponse aiAnalysisResponse=modelMapperService.forResponse().map(aiAnalysis,AIAnalysisResponse.class);

        return aiAnalysisResponse;
    }

    @Override
    public AIAnalysisListResponse getAll() {

        if (aiAnalysisRepository.findAll().isEmpty()){
            throw  new ResourceNotFoundException();
        }
        List<AIAnalysis> aiAnalyses=aiAnalysisRepository.findAll();

        List<AIAnalysisResponse> aiAnalysisResponses=aiAnalyses.stream()
                .map(aiAnalysis -> modelMapperService.forResponse().map(aiAnalysis,AIAnalysisResponse.class))
                .toList();


        AIAnalysisListResponse aiAnalysisListResponse=AIAnalysisListResponse.builder()
                .items(aiAnalysisResponses)
                .count(aiAnalysisResponses.size())
                .build();

        return aiAnalysisListResponse;
    }


    @Override
    public AIAnalysisResponse create(Long complaintId) {
        log.info("1 Ai");

        Complaint complaint=complaintRepository.findById(complaintId)
                .orElseThrow(()->new ResourceNotFoundException("Complaint","Id",complaintId));

        log.info("2 Ai");

        Long predictedServiceDomainId = 1L;
        Long predictedDepartmentId = 1L;

        log.info("3 Ai");


        AIAnalysis aiAnalysis= AIAnalysis.builder()
                .complaint(complaint)
                .createdAt(OffsetDateTime.now())
                .confidenceScore(0.8f)
                .riskLevel("LOW")
                .priority("MEDIUM")
                .summary("Deneme Yapıyorum")
                .build();

        log.info("4 Ai");

        aiAnalysisRepository.save(aiAnalysis);

        log.info("5 Ai");

        AIAnalysisResponse aiAnalysisResponse = modelMapperService.forResponse().map(aiAnalysis,AIAnalysisResponse.class);
        aiAnalysisResponse.setServiceDomainId(predictedServiceDomainId);
        aiAnalysisResponse.setDepartmentId(predictedDepartmentId);

        log.info("6 Ai");

        return aiAnalysisResponse;
    }


}
