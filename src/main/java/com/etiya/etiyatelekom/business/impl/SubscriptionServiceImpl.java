package com.etiya.etiyatelekom.business.impl;

import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.ServiceDomain;
import com.etiya.etiyatelekom.entity.Subscription;
import com.etiya.etiyatelekom.repository.SubscriptionRepository;
import com.etiya.etiyatelekom.business.abst.ServiceDomainService;
import com.etiya.etiyatelekom.business.abst.SubscriptionService;
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
    private final ServiceDomainService serviceDomainService;


    @Override
    public SubscriptionResponse create(SubscriptionCreateRequest request) {
        ServiceDomain serviceDomain=serviceDomainService.getActiveEntityById(request.getServiceDomainId());

        Subscription subscription = Subscription.builder()
                .serviceDomain(serviceDomain)
                .packageName(request.getPackageName())
                .createDate(LocalDate.now())
                .price(request.getPrice())
                .durationDays(request.getDurationDays())
                .build();

        Subscription saved = subscriptionRepository.save(subscription);


        SubscriptionResponse subscriptionResponse= modelMapperService.forResponse().map(saved, SubscriptionResponse.class);

        subscriptionResponse.setServiceType(serviceDomain.getName());

        return subscriptionResponse;

    }

    @Override
    public SubscriptionResponse update(Long id, SubscriptionUpdateRequest request) {
        ServiceDomain serviceDomain=serviceDomainService.getActiveEntityById(request.getServiceDomainId());

        Subscription subscription=subscriptionRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Subscription","Id",id));

        subscription.setPackageName(request.getPackageName());
        subscription.setServiceDomain(serviceDomain);
        subscription.setPrice(request.getPrice());
        subscription.setDurationDays(request.getDurationDays());

        subscriptionRepository.save(subscription);
        SubscriptionResponse response= modelMapperService.forResponse().map(subscription,SubscriptionResponse.class);

        return response;
    }

    @Override
    public SubscriptionResponse getById(Long id) {
        Subscription subscription=subscriptionRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subscription","Id",id));
        SubscriptionResponse response=modelMapperService.forResponse().map(subscription,SubscriptionResponse.class);
        response.setServiceType(subscription.getServiceDomain().getName());
        response.setActivationDate(subscription.getCreateDate());
        return response;
    }

    @Override
    public SubscriptionListResponse getAll() {

        List<Subscription> subscriptions=subscriptionRepository.findAll();
        List<SubscriptionResponse> subscriptionResponses = subscriptions.stream()
                .map(subscription -> {
                    SubscriptionResponse response =
                            modelMapperService.forResponse()
                                    .map(subscription, SubscriptionResponse.class);

                    response.setServiceType(
                            subscription.getServiceDomain().getName()
                    );
                    response.setActivationDate(subscription.getCreateDate());
                    return response;
                })
                .toList();
        SubscriptionListResponse subscriptionListResponse= new SubscriptionListResponse();
        subscriptionListResponse.setCount(subscriptionResponses.size());
        subscriptionListResponse.setItems(subscriptionResponses);

        return subscriptionListResponse;
    }

    @Override
    public void delete(Long id) {
        Subscription subscription=subscriptionRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subscription","Id",id));
        subscriptionRepository.delete(subscription);
    }

    @Override
    public Subscription getEntityById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "Id", id));
    }

}
