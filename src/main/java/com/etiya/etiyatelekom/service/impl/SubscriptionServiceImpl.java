package com.etiya.etiyatelekom.service.impl;

import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.Customer;
import com.etiya.etiyatelekom.entity.Subscription;
import com.etiya.etiyatelekom.repository.CustomerRepository;
import com.etiya.etiyatelekom.repository.SubscriptionRepository;
import com.etiya.etiyatelekom.service.abst.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ModelMapperService modelMapperService;
    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;


    @Override
    public SubscriptionResponse create(SubscriptionCreateRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer", "id", request.getCustomerId()));

        Subscription subscription = Subscription.builder()
                .customer(customer)
                .serviceType(request.getServiceType())
                .packageName(request.getPackageName())
                .status("ACTIVE")
                .activationDate(LocalDate.now())
                .build();

        Subscription saved = subscriptionRepository.save(subscription);

        return modelMapperService.forResponse()
                .map(saved, SubscriptionResponse.class);
    }

    @Override
    public SubscriptionResponse update(Long id, SubscriptionUpdateRequest request) {
        Subscription subscription=subscriptionRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Subscription","Id",id));

        subscription.setPackageName(request.getPackageName());
        subscription.setServiceType(request.getServiceType());
        subscriptionRepository.save(subscription);
        SubscriptionResponse response= modelMapperService.forResponse().map(subscription,SubscriptionResponse.class);

        return response;
    }

    @Override
    public SubscriptionResponse getById(Long id) {
        Subscription subscription=subscriptionRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subscription","Id",id));
        SubscriptionResponse response=modelMapperService.forResponse().map(subscription,SubscriptionResponse.class);
        return response;
    }

    @Override
    public SubscriptionListResponse getAll() {
        if (subscriptionRepository.findAll().isEmpty()){
            throw new ResourceNotFoundException();
        }
        List<Subscription> subscriptions=subscriptionRepository.findAll();
        List<SubscriptionResponse> subscriptionResponses=subscriptions.stream()
                .map(subscription -> modelMapperService.forResponse().map(subscription,SubscriptionResponse.class))
                .toList();
        SubscriptionListResponse subscriptionListResponse= new SubscriptionListResponse();
        subscriptionListResponse.setCount(subscriptionResponses.size());
        subscriptionListResponse.setItems(subscriptionResponses);

        return subscriptionListResponse;
    }

    @Override
    public SubscriptionListResponse getByCustomer(Long customerId) {
        if (!subscriptionRepository.existsByCustomerId(customerId)){
            throw new ResourceNotFoundException("Customer","Id",customerId);
        }
        List<Subscription> subscriptions= subscriptionRepository.findByCustomer_Id(customerId);
        List<SubscriptionResponse> subscriptionResponses=subscriptions.stream()
                .map(subscription -> modelMapperService.forResponse().map(subscription,SubscriptionResponse.class))
                .toList();
        SubscriptionListResponse subscriptionListResponse= new SubscriptionListResponse();
        subscriptionListResponse.setCount(subscriptionResponses.size());
        subscriptionListResponse.setItems(subscriptionResponses);

        return subscriptionListResponse;
    }

    @Override
    public SubscriptionListResponse getByStatus(String status) {

        if (!subscriptionRepository.existsByStatus(status)){
            throw new ResourceNotFoundException("Subscription","Status",status);
        }
        List<Subscription> subscriptions= subscriptionRepository.findByStatus(status);
        List<SubscriptionResponse> subscriptionResponses=subscriptions.stream()
                .map(subscription -> modelMapperService.forResponse().map(subscription,SubscriptionResponse.class))
                .toList();
        SubscriptionListResponse subscriptionListResponse= new SubscriptionListResponse();
        subscriptionListResponse.setCount(subscriptionResponses.size());
        subscriptionListResponse.setItems(subscriptionResponses);

        return subscriptionListResponse;

    }

    @Override
    public void deactivate(Long id) {
        Subscription subscription=subscriptionRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subscription","Id",id));
        subscription.setStatus("Deactive");
        subscriptionRepository.save(subscription);
    }
}
