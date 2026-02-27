package com.etiya.etiyatelekom.business.impl;


import com.etiya.etiyatelekom.api.dto.request.complaintRequest.ComplaintCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintListResponse;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.*;
import com.etiya.etiyatelekom.repository.*;
import com.etiya.etiyatelekom.business.abst.AIAnalysisService;
import com.etiya.etiyatelekom.business.abst.ComplaintService;
import com.etiya.etiyatelekom.business.abst.CustomerService;
import com.etiya.etiyatelekom.business.abst.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final CustomerService customerService;
    private final ModelMapperService modelMapperService;
    private final AIAnalysisService aiAnalysisService;
    private final TicketService ticketService;


    @Override
    public ComplaintResponse create(ComplaintCreateRequest request) {

        Customer customer =customerService.getEntityById(request.getCustomerId());

        Complaint complaint=Complaint.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .customer(customer)
                .createdAt(OffsetDateTime.now())
                .build();

        complaint.setIsActive(true);
        complaintRepository.save(complaint);

        AIAnalysisResponse aiAnalysisResponse=aiAnalysisService.create(complaint);

        Ticket ticket = null;
        boolean isRelevant = aiAnalysisResponse.getIsRelevantComplaint() != Boolean.FALSE;
        if (isRelevant && !isInvalidAnalysisResult(aiAnalysisResponse)) {
            ticket = ticketService.create(aiAnalysisResponse);
        }

        AIAnalysis analysis = aiAnalysisService.getEntityById(aiAnalysisResponse.getId());
        complaint.setAiAnalysis(analysis);
        complaint.setTicket(ticket);

        complaintRepository.save(complaint);

        ComplaintResponse complaintResponse = modelMapperService.forResponse().map(complaint, ComplaintResponse.class);
        complaintResponse.setDepartmentId(aiAnalysisResponse.getDepartmentId());
        complaintResponse.setServiceDomainId(aiAnalysisResponse.getServiceDomainId());
        if (!isRelevant) {
            complaintResponse.setValidationMessage(
                    "Bu içerik geçerli bir telekomünikasyon şikayeti olarak kabul edilmedi. Lütfen internet, telefon, fatura veya hizmetinizle ilgili sorununuzu açıklayın.");
        }
        return complaintResponse;
    }

    private boolean isInvalidAnalysisResult(AIAnalysisResponse aiAnalysisResponse) {
        return aiAnalysisResponse != null
                && Long.valueOf(1L).equals(aiAnalysisResponse.getDepartmentId())
                && Long.valueOf(1L).equals(aiAnalysisResponse.getServiceDomainId());
    }

    @Override
    public ComplaintResponse getById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "Id", id));
        return toComplaintResponse(complaint);
    }

    @Override
    public ComplaintListResponse getAll() {
        List<Complaint> complaints = complaintRepository.findAll();
        List<ComplaintResponse> complaintResponses = complaints.stream()
                .map(this::toComplaintResponse)
                .toList();
        return ComplaintListResponse.builder()
                .items(complaintResponses)
                .count(complaintResponses.size())
                .build();
    }

    @Override
    public ComplaintListResponse getActive() {
        List<Complaint> complaints = complaintRepository.findByIsActiveTrue();
        List<ComplaintResponse> complaintResponses = complaints.stream()
                .map(this::toComplaintResponse)
                .toList();
        return ComplaintListResponse.builder()
                .items(complaintResponses)
                .count(complaintResponses.size())
                .build();
    }

    private ComplaintResponse toComplaintResponse(Complaint complaint) {
        ComplaintResponse response = modelMapperService.forResponse().map(complaint, ComplaintResponse.class);
        if (complaint.getTicket() != null) {
            if (complaint.getTicket().getDepartment() != null) {
                response.setDepartmentId(complaint.getTicket().getDepartment().getId());
            }
            if (complaint.getTicket().getServiceDomain() != null) {
                response.setServiceDomainId(complaint.getTicket().getServiceDomain().getId());
            }
        }
        return response;
    }

    @Override
    public ComplaintListResponse getByCustomer(Long customerId) {

        List<Complaint> complaints = complaintRepository.findByCustomerIdAndIsActiveTrue(customerId);
        List<ComplaintResponse> items = complaints.stream()
                .map(this::toComplaintResponse)
                .toList();

        ComplaintListResponse complaintListResponse=ComplaintListResponse.builder()
                .items(items)
                .count(items.size())
                .build();

        return complaintListResponse;
    }

    @Override
    public Complaint getEntityById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "Id", id));
    }


}
