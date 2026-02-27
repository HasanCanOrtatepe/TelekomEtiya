package com.etiya.etiyatelekom.business.abst;

import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionResponse;
import com.etiya.etiyatelekom.entity.Subscription;

public interface SubscriptionService {

    SubscriptionResponse create(SubscriptionCreateRequest request);

    SubscriptionResponse update(Long id, SubscriptionUpdateRequest request);

    SubscriptionResponse getById(Long id);

    SubscriptionListResponse getAll();

    void delete(Long id);

    Subscription getEntityById(Long id);

}
