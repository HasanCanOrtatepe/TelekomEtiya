package com.etiya.etiyatelekom.api.controller;


import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.subscriptionRequest.SubscriptionUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionListResponse;
import com.etiya.etiyatelekom.api.dto.response.subscriptionResponse.SubscriptionResponse;
import com.etiya.etiyatelekom.service.abst.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
@PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(@Valid @RequestBody SubscriptionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> update(@PathVariable Long id,
                                                       @Valid @RequestBody SubscriptionUpdateRequest request) {
        return ResponseEntity.ok(subscriptionService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<SubscriptionListResponse> getAll() {
        return ResponseEntity.ok(subscriptionService.getAll());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
