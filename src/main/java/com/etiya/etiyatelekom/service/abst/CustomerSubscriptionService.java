package com.etiya.etiyatelekom.service.abst;


import com.etiya.etiyatelekom.api.dto.request.customerSubscriptionRequest.CustomerSubscriptionExtendRequest;
import com.etiya.etiyatelekom.api.dto.request.customerSubscriptionRequest.CustomerSubscriptionPurchaseRequest;
import com.etiya.etiyatelekom.api.dto.response.customerSubscriptionResponse.CustomerSubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.customerSubscriptionResponse.CustomerSubscriptionResponse;

public interface CustomerSubscriptionService {

    CustomerSubscriptionResponse purchase(CustomerSubscriptionPurchaseRequest request);

    CustomerSubscriptionResponse cancel(Long customerSubscriptionId);

    CustomerSubscriptionResponse extend(Long customerSubscriptionId, CustomerSubscriptionExtendRequest request);

    CustomerSubscriptionListResponse getByCustomer(Long customerId);

    CustomerSubscriptionListResponse getActiveByCustomer(Long customerId);

    CustomerSubscriptionResponse getById(Long customerSubscriptionId);

    CustomerSubscriptionListResponse getAll();


}
