package com.etiya.etiyatelekom.service.impl;

import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisListResponse;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;
import com.etiya.etiyatelekom.common.enums.TicketPriorityEnums;
import com.etiya.etiyatelekom.common.enums.TicketRiskLevelEnums;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.AIAnalysis;
import com.etiya.etiyatelekom.entity.Complaint;
import com.etiya.etiyatelekom.entity.ServiceDomain;
import com.etiya.etiyatelekom.repository.AIAnalysisRepository;
import com.etiya.etiyatelekom.repository.ComplaintRepository;
import com.etiya.etiyatelekom.repository.DepartmentRepository;
import com.etiya.etiyatelekom.repository.ServiceDomainRepository;
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
    private final ServiceDomainRepository serviceDomainRepository;
    private final DepartmentRepository departmentRepository;

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

        Complaint complaint=complaintRepository.findById(complaintId)
                .orElseThrow(()->new ResourceNotFoundException("Complaint","Id",complaintId));

        if (!departmentRepository.existsByIdIsNotNull()){
            throw new ResourceNotFoundException("Department","System","System not ready wait please");
        }
        if (!serviceDomainRepository.existsByIdIsNotNull()){
            throw new ResourceNotFoundException("Department","System","System not ready wait please");
        }

        Long predictedServiceDomainId = 1L;
        Long predictedDepartmentId = 1L;



        AIAnalysis aiAnalysis= AIAnalysis.builder()
                .complaint(complaint)
                .createdAt(OffsetDateTime.now())
                .confidenceScore(0.8f)
                .riskLevel(TicketRiskLevelEnums.LOW)
                .priority(TicketPriorityEnums.MEDIUM)
                .summary("Deneme Yapıyorum")
                .build();

        aiAnalysisRepository.save(aiAnalysis);

        AIAnalysisResponse aiAnalysisResponse = modelMapperService.forResponse().map(aiAnalysis,AIAnalysisResponse.class);
        aiAnalysisResponse.setServiceDomainId(predictedServiceDomainId);
        aiAnalysisResponse.setDepartmentId(predictedDepartmentId);

        return aiAnalysisResponse;
    }


}
