package com.etiya.etiyatelekom.business.impl;


import com.etiya.etiyatelekom.api.dto.request.customerSubscriptionRequest.CustomerSubscriptionExtendRequest;
import com.etiya.etiyatelekom.api.dto.request.customerSubscriptionRequest.CustomerSubscriptionPurchaseRequest;
import com.etiya.etiyatelekom.api.dto.response.customerSubscriptionResponse.CustomerSubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.customerSubscriptionResponse.CustomerSubscriptionResponse;
import com.etiya.etiyatelekom.common.enums.SubscriptionStatus;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceAlreadyExistsException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.entity.Customer;
import com.etiya.etiyatelekom.entity.CustomerSubscription;
import com.etiya.etiyatelekom.entity.Subscription;
import com.etiya.etiyatelekom.repository.CustomerSubscriptionRepository;
import com.etiya.etiyatelekom.business.abst.CustomerService;
import com.etiya.etiyatelekom.business.abst.CustomerSubscriptionService;
import com.etiya.etiyatelekom.business.abst.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerSubscriptionServiceImpl implements CustomerSubscriptionService {

    private final CustomerService customerService;
    private final CustomerSubscriptionRepository customerSubscriptionRepository;
    private final SubscriptionService subscriptionService;


    @Override
    public CustomerSubscriptionResponse purchase(CustomerSubscriptionPurchaseRequest request) {

        Customer customer = customerService.getEntityById(request.getCustomerId());
        Subscription subscription = subscriptionService.getEntityById(request.getSubscriptionId());

        var existing = customerSubscriptionRepository
                .findByCustomerIdAndSubscriptionId(request.getCustomerId(), request.getSubscriptionId());

        if (existing.isPresent()) {
            CustomerSubscription cs = existing.get();
            if (cs.getStatus() == SubscriptionStatus.ACTIVE) {
                throw new ResourceAlreadyExistsException(
                        "Customer Subscription", "Customer", "Subscription");
            }
            cs.setStatus(SubscriptionStatus.ACTIVE);
            cs.setStartDate(OffsetDateTime.now());
            cs.setEndDate(OffsetDateTime.now().plusDays(subscription.getDurationDays()));
            customerSubscriptionRepository.save(cs);
            return mapToResponse(cs);
        }

        CustomerSubscription customerSubscription = CustomerSubscription.builder()
                .subscription(subscription)
                .customer(customer)
                .startDate(OffsetDateTime.now())
                .endDate(OffsetDateTime.now().plusDays(subscription.getDurationDays()))
                .status(SubscriptionStatus.ACTIVE)
                .build();

        customerSubscriptionRepository.save(customerSubscription);

        return mapToResponse(customerSubscription);
    }

    @Override
    public CustomerSubscriptionResponse cancel(Long customerSubscriptionId) {

        CustomerSubscription customerSubscription = getEntityById(customerSubscriptionId);

        customerSubscription.setStatus(SubscriptionStatus.CANCELLED);
        customerSubscriptionRepository.save(customerSubscription);

        return mapToResponse(customerSubscription);
    }

    @Override
    public CustomerSubscriptionResponse extend(Long customerSubscriptionId, CustomerSubscriptionExtendRequest request) {

        CustomerSubscription customerSubscription = getEntityById(customerSubscriptionId);

        customerSubscription.setEndDate(
                customerSubscription.getEndDate().plusDays(request.getDays()));

        customerSubscriptionRepository.save(customerSubscription);

        return mapToResponse(customerSubscription);
    }

    @Override
    public CustomerSubscriptionListResponse getByCustomer(Long customerId) {

        List<CustomerSubscriptionResponse> responses =
                customerSubscriptionRepository.findByCustomerId(customerId)
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return CustomerSubscriptionListResponse.builder()
                .items(responses)
                .count(responses.size())
                .build();
    }

    @Override
    public CustomerSubscriptionListResponse getActiveByCustomer(Long customerId) {

        List<CustomerSubscriptionResponse> responses =
                customerSubscriptionRepository
                        .findByCustomerIdAndStatus(customerId, SubscriptionStatus.ACTIVE)
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return CustomerSubscriptionListResponse.builder()
                .items(responses)
                .count(responses.size())
                .build();
    }

    @Override
    public CustomerSubscriptionResponse getById(Long customerSubscriptionId) {
        return mapToResponse(getEntityById(customerSubscriptionId));
    }

    @Override
    public CustomerSubscriptionListResponse getAll() {

        List<CustomerSubscriptionResponse> responses =
                customerSubscriptionRepository.findAll()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return CustomerSubscriptionListResponse.builder()
                .items(responses)
                .count(responses.size())
                .build();
    }

    private CustomerSubscription getEntityById(Long id) {
        return customerSubscriptionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("CustomerSubscription", "Id", id));
    }

    private CustomerSubscriptionResponse mapToResponse(CustomerSubscription customerSubscription) {
        return CustomerSubscriptionResponse.builder()
                .id(customerSubscription.getId())
                .customerId(customerSubscription.getCustomer().getId())
                .subscriptionId(customerSubscription.getSubscription().getId())
                .serviceType(customerSubscription.getSubscription().getServiceDomain().getName())
                .packageName(customerSubscription.getSubscription().getPackageName())
                .startDate(customerSubscription.getStartDate())
                .endDate(customerSubscription.getEndDate())
                .status(customerSubscription.getStatus())
                .build();
    }

}