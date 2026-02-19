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
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final CustomerRepository customerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ModelMapperService modelMapperService;
    private final AIAnalysisService aiAnalysisService;
    private final DepartmentRepository departmentRepository;
    private final ServiceDomainRepository serviceDomainRepository;
    private final TicketRepository ticketRepository;


    @Override
    public ComplaintResponse create(ComplaintCreateRequest request) {

        Customer customer =customerRepository.findById(request.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("Customer","Id",request.getCustomerId()));

        Complaint complaint=modelMapperService.forRequest().map(request,Complaint.class);
        complaint.setCustomer(customer);
        complaint.setCreatedAt(OffsetDateTime.now());

        if(request.getSubscriptionId()!=null){
            complaint.setSubscription(subscriptionRepository.findById(request.getSubscriptionId())
                    .orElseThrow(()->new ResourceNotFoundException("Subscription","Id", request.getSubscriptionId())));
        }
        complaintRepository.save(complaint);

        AIAnalysisResponse aiAnalysisResponse=aiAnalysisService.create(complaint.getId());

        log.info("Burda Hata var");
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
                .status("Open")
                .riskLevel(aiAnalysisResponse.getRiskLevel())
                .serviceDomain(serviceDomain)
                .build();

        ticketRepository.save(ticket);

        ComplaintResponse complaintResponse=modelMapperService.forResponse().map(complaint,ComplaintResponse.class);

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

        if (customerRepository.findAll().isEmpty()){
            throw new ResourceNotFoundException();
        }
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

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer", "Id", customerId);
        }

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
}
