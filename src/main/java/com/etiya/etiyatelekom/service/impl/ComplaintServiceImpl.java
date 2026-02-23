package com.etiya.etiyatelekom.service.impl;


import com.etiya.etiyatelekom.api.dto.request.complaintRequest.ComplaintCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintListResponse;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.*;
import com.etiya.etiyatelekom.repository.*;
import com.etiya.etiyatelekom.service.abst.AIAnalysisService;
import com.etiya.etiyatelekom.service.abst.ComplaintService;
import com.etiya.etiyatelekom.service.abst.CustomerService;
import com.etiya.etiyatelekom.service.abst.TicketService;
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

        complaintRepository.save(complaint);

        AIAnalysisResponse aiAnalysisResponse=aiAnalysisService.create(complaint);
        log.info(aiAnalysisResponse.toString());

        Ticket ticket= ticketService.create(aiAnalysisResponse);

        AIAnalysis analysis = aiAnalysisService.getEntityById(aiAnalysisResponse.getId());
        complaint.setAiAnalysis(analysis);
        complaint.setTicket(ticket);

        ComplaintResponse complaintResponse=modelMapperService.forResponse().map(complaint,ComplaintResponse.class);

        complaintRepository.save(complaint);
        return complaintResponse;
    }

    @Override
    public ComplaintResponse getById(Long id) {
        Complaint complaint=complaintRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Complaint","Id",id));

        ComplaintResponse complaintResponse = modelMapperService.forResponse().map(complaint,ComplaintResponse.class);

        return complaintResponse;

    }

    @Override
    public ComplaintListResponse getAll() {

        List<Complaint> complaints=complaintRepository.findAll();
        List<ComplaintResponse> complaintResponses=complaints.stream()
                .map(complaint -> modelMapperService.forResponse().map(complaint,ComplaintResponse.class))
                .toList();

        ComplaintListResponse complaintListResponse=ComplaintListResponse.builder()
                .items(complaintResponses)
                .count(complaintResponses.size())
                .build();

        return complaintListResponse;
    }

    @Override
    public ComplaintListResponse getByCustomer(Long customerId) {

        List<Complaint> complaints = complaintRepository.findByCustomerId(customerId);

        List<ComplaintResponse> items = complaints.stream()
                .map(c -> modelMapperService.forResponse().map(c, ComplaintResponse.class))
                .toList();

        ComplaintListResponse complaintListResponse=ComplaintListResponse.builder()
                .items(items)
                .count(items.size())
                .build();

        return complaintListResponse;
    }

    @Override
    public void delete(Long id) {
        Complaint complaint=complaintRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Complaint","Id",id));

        complaintRepository.delete(complaint);

    }

    @Override
    public Complaint getEntityById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "Id", id));
    }


}
