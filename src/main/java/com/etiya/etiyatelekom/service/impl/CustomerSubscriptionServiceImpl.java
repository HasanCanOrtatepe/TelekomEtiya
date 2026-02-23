package com.etiya.etiyatelekom.service.impl;


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
import com.etiya.etiyatelekom.repository.CustomerRepository;
import com.etiya.etiyatelekom.repository.CustomerSubscriptionRepository;
import com.etiya.etiyatelekom.repository.SubscriptionRepository;
import com.etiya.etiyatelekom.service.abst.CustomerService;
import com.etiya.etiyatelekom.service.abst.CustomerSubscriptionService;
import com.etiya.etiyatelekom.service.abst.SubscriptionService;
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

        if (customerSubscriptionRepository.existsByCustomerIdAndSubscriptionId(request.getCustomerId(),request.getSubscriptionId())){
            throw new ResourceAlreadyExistsException("Customer Subscription","Customer","Subscription");
        }
        CustomerSubscription customerSubscription=CustomerSubscription.builder()
                .subscription(subscription)
                .customer(customer)
                .startDate(OffsetDateTime.now())
                .endDate(OffsetDateTime.now().plusDays(subscription.getDurationDays()))
                .status(SubscriptionStatus.ACTIVE)
                .build();

        customerSubscriptionRepository.save(customerSubscription);

        CustomerSubscriptionResponse customerSubscriptionResponse=CustomerSubscriptionResponse.builder()
                .id(customerSubscription.getId())
                .customerId(customerSubscription.getCustomer().getId())
                .subscriptionId(customerSubscription.getSubscription().getId())
                .serviceType(customerSubscription.getSubscription().getServiceDomain().getName())
                .packageName(customerSubscription.getSubscription().getPackageName())
                .startDate(customerSubscription.getStartDate())
                .endDate(customerSubscription.getEndDate())
                .status(customerSubscription.getStatus())
                .build();
        return customerSubscriptionResponse;
    }

    @Override
    public CustomerSubscriptionResponse cancel(Long customerSubscriptionId) {
        CustomerSubscription customerSubscription = customerSubscriptionRepository.findById(customerSubscriptionId)
                .orElseThrow(()->new ResourceNotFoundException("CustomerSubscription","Id",customerSubscriptionId));
        customerSubscription.setStatus(SubscriptionStatus.CANCELLED);

        CustomerSubscriptionResponse customerSubscriptionResponse=CustomerSubscriptionResponse.builder()
                .id(customerSubscription.getId())
                .customerId(customerSubscription.getCustomer().getId())
                .subscriptionId(customerSubscription.getSubscription().getId())
                .serviceType(customerSubscription.getSubscription().getServiceDomain().getName())
                .packageName(customerSubscription.getSubscription().getPackageName())
                .startDate(customerSubscription.getStartDate())
                .endDate(customerSubscription.getEndDate())
                .status(customerSubscription.getStatus())
                .build();

        return customerSubscriptionResponse;
    }

    @Override
    public CustomerSubscriptionResponse extend(Long customerSubscriptionId, CustomerSubscriptionExtendRequest request) {

        CustomerSubscription customerSubscription = customerSubscriptionRepository.findById(customerSubscriptionId)
                .orElseThrow(()->new ResourceNotFoundException("CustomerSubscription","Id",customerSubscriptionId));
        customerSubscription.setEndDate(customerSubscription.getEndDate().plusDays(request.getDays()));

        CustomerSubscriptionResponse customerSubscriptionResponse=CustomerSubscriptionResponse.builder()
                .id(customerSubscription.getId())
                .customerId(customerSubscription.getCustomer().getId())
                .subscriptionId(customerSubscription.getSubscription().getId())
                .serviceType(customerSubscription.getSubscription().getServiceDomain().getName())
                .packageName(customerSubscription.getSubscription().getPackageName())
                .startDate(customerSubscription.getStartDate())
                .endDate(customerSubscription.getEndDate())
                .status(customerSubscription.getStatus())
                .build();

        return customerSubscriptionResponse;
    }

    @Override
    public CustomerSubscriptionListResponse getByCustomer(Long customerId) {

        List<CustomerSubscriptionResponse> customerSubscriptionResponses=customerSubscriptionRepository.findByCustomerId(customerId)
                .stream().map(customerSubscription -> CustomerSubscriptionResponse.builder()
                        .id(customerSubscription.getId())
                        .customerId(customerSubscription.getCustomer().getId())
                        .subscriptionId(customerSubscription.getSubscription().getId())
                        .serviceType(customerSubscription.getSubscription().getServiceDomain().getName())
                        .packageName(customerSubscription.getSubscription().getPackageName())
                        .startDate(customerSubscription.getStartDate())
                        .endDate(customerSubscription.getEndDate())
                        .status(customerSubscription.getStatus())
                        .build() ).toList();

        CustomerSubscriptionListResponse customerSubscriptionListResponse=CustomerSubscriptionListResponse.builder()
                .items(customerSubscriptionResponses)
                .count(customerSubscriptionResponses.size())
                .build();

        return customerSubscriptionListResponse;
    }

    @Override
    public CustomerSubscriptionListResponse getActiveByCustomer(Long customerId) {

        List<CustomerSubscriptionResponse> customerSubscriptionResponses=customerSubscriptionRepository.findByCustomerIdAndStatus(customerId,"Active")
                .stream().map(customerSubscription -> CustomerSubscriptionResponse.builder()
                        .id(customerSubscription.getId())
                        .customerId(customerSubscription.getCustomer().getId())
                        .subscriptionId(customerSubscription.getSubscription().getId())
                        .serviceType(customerSubscription.getSubscription().getServiceDomain().getName())
                        .packageName(customerSubscription.getSubscription().getPackageName())
                        .startDate(customerSubscription.getStartDate())
                        .endDate(customerSubscription.getEndDate())
                        .status(customerSubscription.getStatus())
                        .build() ).toList();

        CustomerSubscriptionListResponse customerSubscriptionListResponse=CustomerSubscriptionListResponse.builder()
                .items(customerSubscriptionResponses)
                .count(customerSubscriptionResponses.size())
                .build();

        return customerSubscriptionListResponse;

    }

    @Override
    public CustomerSubscriptionResponse getById(Long customerSubscriptionId) {

        CustomerSubscription customerSubscription = customerSubscriptionRepository.findById(customerSubscriptionId)
                .orElseThrow(()-> new ResourceNotFoundException("Customer Subscription","Id",customerSubscriptionId));

        CustomerSubscriptionResponse customerSubscriptionResponse=CustomerSubscriptionResponse.builder()
                .id(customerSubscription.getId())
                .customerId(customerSubscription.getCustomer().getId())
                .subscriptionId(customerSubscription.getSubscription().getId())
                .serviceType(customerSubscription.getSubscription().getServiceDomain().getName())
                .packageName(customerSubscription.getSubscription().getPackageName())
                .startDate(customerSubscription.getStartDate())
                .endDate(customerSubscription.getEndDate())
                .status(customerSubscription.getStatus())
                .build();

        return customerSubscriptionResponse;
    }

    @Override
    public CustomerSubscriptionListResponse getAll() {

        List<CustomerSubscriptionResponse> customerSubscriptionResponses=customerSubscriptionRepository.findAll()
                .stream().map(customerSubscription -> CustomerSubscriptionResponse.builder()
                        .id(customerSubscription.getId())
                        .customerId(customerSubscription.getCustomer().getId())
                        .subscriptionId(customerSubscription.getSubscription().getId())
                        .serviceType(customerSubscription.getSubscription().getServiceDomain().getName())
                        .packageName(customerSubscription.getSubscription().getPackageName())
                        .startDate(customerSubscription.getStartDate())
                        .endDate(customerSubscription.getEndDate())
                        .status(customerSubscription.getStatus())
                        .build() ).toList();

        CustomerSubscriptionListResponse customerSubscriptionListResponse=CustomerSubscriptionListResponse.builder()
                .items(customerSubscriptionResponses)
                .count(customerSubscriptionResponses.size())
                .build();

        return customerSubscriptionListResponse;

    }

}
