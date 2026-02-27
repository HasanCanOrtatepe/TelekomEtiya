package com.etiya.etiyatelekom.api.controller;

import com.etiya.etiyatelekom.api.dto.request.customerSubscriptionRequest.CustomerSubscriptionExtendRequest;
import com.etiya.etiyatelekom.api.dto.request.customerSubscriptionRequest.CustomerSubscriptionPurchaseRequest;
import com.etiya.etiyatelekom.api.dto.response.customerSubscriptionResponse.CustomerSubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.customerSubscriptionResponse.CustomerSubscriptionResponse;
import com.etiya.etiyatelekom.business.abst.CustomerSubscriptionService;
import com.etiya.etiyatelekom.security.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer-subscriptions")
@PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT','CUSTOMER')")
public class CustomerSubscriptionController {

    private final CustomerSubscriptionService customerSubscriptionService;

    @PostMapping
    public ResponseEntity<CustomerSubscriptionResponse> purchase(@Valid @RequestBody CustomerSubscriptionPurchaseRequest request) {
        CustomerSubscriptionPurchaseRequest effectiveRequest = request;
        if (SecurityUtils.isCustomer()) {
            Long customerId = SecurityUtils.getCurrentUserId();
            if (customerId == null) throw new AccessDeniedException("Kimlik doğrulanamadı");
            effectiveRequest = CustomerSubscriptionPurchaseRequest.builder()
                    .customerId(customerId)
                    .subscriptionId(request.getSubscriptionId())
                    .build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(customerSubscriptionService.purchase(effectiveRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerSubscriptionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerSubscriptionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<CustomerSubscriptionListResponse> getAll() {
        return ResponseEntity.ok(customerSubscriptionService.getAll());
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<CustomerSubscriptionListResponse> getByCustomer(@PathVariable Long id) {
        if (SecurityUtils.isCustomer() && !id.equals(SecurityUtils.getCurrentUserId())) {
            throw new AccessDeniedException("Sadece kendi aboneliklerinize erişebilirsiniz");
        }
        return ResponseEntity.ok(customerSubscriptionService.getByCustomer(id));
    }
    @GetMapping("/customer/active/{id}")
    public ResponseEntity<CustomerSubscriptionListResponse> getActiveByCustomer(@PathVariable Long id) {
        if (SecurityUtils.isCustomer() && !id.equals(SecurityUtils.getCurrentUserId())) {
            throw new AccessDeniedException("Sadece kendi aboneliklerinize erişebilirsiniz");
        }
        return ResponseEntity.ok(customerSubscriptionService.getActiveByCustomer(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<CustomerSubscriptionResponse> cancel(@PathVariable Long id) {
        if (SecurityUtils.isCustomer()) {
            CustomerSubscriptionResponse sub = customerSubscriptionService.getById(id);
            if (sub == null || !sub.getCustomerId().equals(SecurityUtils.getCurrentUserId())) {
                throw new AccessDeniedException("Sadece kendi aboneliklerinizi iptal edebilirsiniz");
            }
        }
        return ResponseEntity.ok(customerSubscriptionService.cancel(id));
    }

    @PatchMapping("/{id}/extend")
    public ResponseEntity<CustomerSubscriptionResponse> extend(@PathVariable Long id, @Valid @RequestBody CustomerSubscriptionExtendRequest request) {
        return ResponseEntity.ok(customerSubscriptionService.extend(id,request));
    }


}
