package com.etiya.etiyatelekom.service.impl;


import com.etiya.etiyatelekom.api.dto.request.complaintRequest.ComplaintCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintListResponse;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintResponse;
import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.*;
import com.etiya.etiyatelekom.repository.*;
import com.etiya.etiyatelekom.service.abst.AIAnalysisService;
import com.etiya.etiyatelekom.service.abst.ComplaintService;
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
    private final CustomerRepository customerRepository;
    private final ModelMapperService modelMapperService;
    private final AIAnalysisService aiAnalysisService;
    private final DepartmentRepository departmentRepository;
    private final ServiceDomainRepository serviceDomainRepository;
    private final TicketRepository ticketRepository;
    private final TicketStatusHistoryRepository ticketStatusHistoryRepository;


    @Override
    public ComplaintResponse create(ComplaintCreateRequest request) {

        Customer customer =customerRepository.findById(request.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("Customer","Id",request.getCustomerId()));

        Complaint complaint=Complaint.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .customer(customer)
                .createdAt(OffsetDateTime.now())
                .build();

        log.info(" 4  Burda ");
        complaintRepository.save(complaint);

        log.info(" 5  Burda ");

        AIAnalysisResponse aiAnalysisResponse=aiAnalysisService.create(complaint.getId());

        log.info(" 6  Burda ");

        Department department=departmentRepository.findById(aiAnalysisResponse.getDepartmentId())
                .orElseThrow(()->new ResourceNotFoundException("Department","Id",aiAnalysisResponse.getDepartmentId()));

        ServiceDomain serviceDomain= serviceDomainRepository.findById(aiAnalysisResponse.getServiceDomainId())
                .orElseThrow(()->new ResourceNotFoundException("Service Domain","Id",aiAnalysisResponse.getServiceDomainId()));

        Ticket ticket=Ticket.builder()
                .createdAt(OffsetDateTime.now())
                .priority(aiAnalysisResponse.getPriority())
                .complaint(complaint)
                .slaDueAt(OffsetDateTime.now().plusHours(department.getSlaHours()))
                .department(department)
                .status(TicketStatusEnums.CREATED)
                .riskLevel(aiAnalysisResponse.getRiskLevel())
                .serviceDomain(serviceDomain)
                .build();

        log.info(" 7  Burda ");
        ticketRepository.save(ticket);
        AIAnalysis analysis= AIAnalysis.builder()
                .complaint(complaint)
                .confidenceScore(aiAnalysisResponse.getConfidenceScore())
                .createdAt(aiAnalysisResponse.getCreatedAt())
                .priority(aiAnalysisResponse.getPriority())
                .riskLevel(aiAnalysisResponse.getRiskLevel())
                .summary(aiAnalysisResponse.getSummary())
                .id(aiAnalysisResponse.getId())
                .build();

        complaint.setAiAnalysis(analysis);
        complaint.setTicket(ticket);

        complaintRepository.save(complaint);

        ComplaintResponse complaintResponse=modelMapperService.forResponse().map(complaint,ComplaintResponse.class);
        changeticket(ticket.getId(),ticket.getStatus());

        log.info(" 8  Burda ");
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

    private void changeticket(Long id,TicketStatusEnums status){

        log.info("1 changeticket");

        Ticket ticket=ticketRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Ticket","Id",id));

        log.info("2 changeticket");


        TicketStatusHistory ticketStatusHistory=TicketStatusHistory.builder()
                .fromStatus(null)
                .ticket(ticket)
                .changedAt(OffsetDateTime.now())
                .toStatus(status)
                .build();
        log.info("3 changeticket");
        ticketStatusHistoryRepository.save(ticketStatusHistory);
        log.info("4 changeticket");
    }

}
