package com.etiya.etiyatelekom.service.abst;

import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionResponse;

public interface SubscriptionService {

    SubscriptionResponse create(SubscriptionCreateRequest request);

    SubscriptionResponse update(Long id, SubscriptionUpdateRequest request);

    SubscriptionResponse getById(Long id);

    SubscriptionListResponse getAll();

    SubscriptionListResponse getByCustomer(Long customerId);

    SubscriptionListResponse getByStatus(String status);

    void deactivate(Long id); // status=PASSIVE
}
