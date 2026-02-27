package com.etiya.etiyatelekom.api.controller;


import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionResponse;
import com.etiya.etiyatelekom.business.abst.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionResponse> create(@Valid @RequestBody SubscriptionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubscriptionResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody SubscriptionUpdateRequest request) {
        return ResponseEntity.ok(subscriptionService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT','CUSTOMER')")
    public ResponseEntity<SubscriptionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT','CUSTOMER')")
    public ResponseEntity<SubscriptionListResponse> getAll() {
        return ResponseEntity.ok(subscriptionService.getAll());
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
