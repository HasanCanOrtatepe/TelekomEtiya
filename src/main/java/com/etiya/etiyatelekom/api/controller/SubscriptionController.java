package com.etiya.etiyatelekom.api.controller;


import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionResponse;
import com.etiya.etiyatelekom.service.abst.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("subscription/deneme")
    public ResponseEntity<SubscriptionResponse> createSubscription(@Valid @RequestBody SubscriptionCreateRequest subscriptionCreateRequest){
        return new ResponseEntity<>(subscriptionService.create(subscriptionCreateRequest),HttpStatus.CREATED);
    }

    @PutMapping("subscription/deneme/{id}")
    public ResponseEntity<SubscriptionResponse> updateSubscription(@Valid @RequestBody SubscriptionUpdateRequest subscriptionUpdateRequest, @PathVariable Long id){
        return new ResponseEntity<>(subscriptionService.update(id,subscriptionUpdateRequest),HttpStatus.OK);
    }

    @PutMapping("subscription/deneme/deactive/{id}")
    public ResponseEntity<Void> deactiveSubscription(@PathVariable Long id){
        subscriptionService.deactivate(id);
        return  ResponseEntity.noContent().build();
    }

    @GetMapping("subscription/deneme/status/{status}")
    public ResponseEntity<SubscriptionListResponse> getByStatusSubscription(@PathVariable String status){
        return  new ResponseEntity<>(subscriptionService.getByStatus(status),HttpStatus.OK);
    }

    @GetMapping("subscription/deneme/customer/{id}")
    public ResponseEntity<SubscriptionListResponse> getByCustomerIdSubscription(@PathVariable Long id){
        return  new ResponseEntity<>(subscriptionService.getByCustomer(id),HttpStatus.OK);
    }
    @GetMapping("subscription/deneme/{id}")
    public ResponseEntity<SubscriptionResponse> getByIdSubscription(@PathVariable Long id){
        return  new ResponseEntity<>(subscriptionService.getById(id),HttpStatus.OK);
    }

    @GetMapping("subscription/deneme")
    public ResponseEntity<SubscriptionListResponse> getAllSubscription(){
        return  new ResponseEntity<>(subscriptionService.getAll(),HttpStatus.OK);
    }



}
