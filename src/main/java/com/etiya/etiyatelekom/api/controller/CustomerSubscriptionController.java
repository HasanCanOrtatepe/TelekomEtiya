package com.etiya.etiyatelekom.api.controller;

import com.etiya.etiyatelekom.api.dto.request.customerRequest.CustomerCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.customerRequest.CustomerUpdateRequest;
import com.etiya.etiyatelekom.api.dto.request.customerSubscriptionRequest.CustomerSubscriptionExtendRequest;
import com.etiya.etiyatelekom.api.dto.request.customerSubscriptionRequest.CustomerSubscriptionPurchaseRequest;
import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponse;
import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponseList;
import com.etiya.etiyatelekom.api.dto.response.customerSubscriptionResponse.CustomerSubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.customerSubscriptionResponse.CustomerSubscriptionResponse;
import com.etiya.etiyatelekom.service.abst.CustomerService;
import com.etiya.etiyatelekom.service.abst.CustomerSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer-subscriptions")
public class CustomerSubscriptionController {

    private final CustomerSubscriptionService customerSubscriptionService;

    @PostMapping
    public ResponseEntity<CustomerSubscriptionResponse> purchase(@Valid @RequestBody CustomerSubscriptionPurchaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerSubscriptionService.purchase(request));
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
        return ResponseEntity.ok(customerSubscriptionService.getByCustomer(id));
    }
    @GetMapping("/customer/active/{id}")
    public ResponseEntity<CustomerSubscriptionListResponse> getActiveByCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerSubscriptionService.getActiveByCustomer(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<CustomerSubscriptionResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(customerSubscriptionService.cancel(id));
    }

    @PatchMapping("/{id}/extend")
    public ResponseEntity<CustomerSubscriptionResponse> extend(@PathVariable Long id, @Valid @RequestBody CustomerSubscriptionExtendRequest request) {
        return ResponseEntity.ok(customerSubscriptionService.extend(id,request));
    }


}
