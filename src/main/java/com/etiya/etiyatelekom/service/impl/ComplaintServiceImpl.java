package com.etiya.etiyatelekom.service.impl;


import com.etiya.etiyatelekom.api.dto.request.complaintRequest.ComplaintCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintListResponse;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.entity.Complaint;
import com.etiya.etiyatelekom.entity.Customer;
import com.etiya.etiyatelekom.entity.Subscription;
import com.etiya.etiyatelekom.repository.ComplaintRepository;
import com.etiya.etiyatelekom.repository.CustomerRepository;
import com.etiya.etiyatelekom.repository.SubscriptionRepository;
import com.etiya.etiyatelekom.service.abst.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final CustomerRepository customerRepository;
    private final SubscriptionRepository subscriptionRepository;


    @Override
    public ComplaintResponse create(ComplaintCreateRequest request) {

        Customer customer=customerRepository.findById(request.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("Customer","Id",request.getCustomerId()));

        Subscription subscription=subscriptionRepository.findById(request.getSubscriptionId())
                .orElseThrow(()-> new ResourceNotFoundException("Subscription","Id",request.getCustomerId()));

        Complaint.builder()
                .createdAt(OffsetDateTime.now())
                .customer(customer).build();

        return null;
    }

    @Override
    public ComplaintResponse getById(Long id) {
        return null;
    }

    @Override
    public ComplaintListResponse getAll() {
        return null;
    }

    @Override
    public ComplaintListResponse getByCustomer(Long customerId) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
