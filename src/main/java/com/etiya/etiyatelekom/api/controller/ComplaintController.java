package com.etiya.etiyatelekom.api.controller;

import com.etiya.etiyatelekom.api.dto.request.complaintRequest.ComplaintCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintListResponse;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintResponse;
import com.etiya.etiyatelekom.business.abst.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/complaints")
@PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT','CUSTOMER')")
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<ComplaintResponse> create(@Valid @RequestBody ComplaintCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(complaintService.create(request));
    }

    @GetMapping
    public ResponseEntity<ComplaintListResponse> getAll() {
        return ResponseEntity.ok(complaintService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<ComplaintListResponse> getActive() {
        return ResponseEntity.ok(complaintService.getActive());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ComplaintListResponse> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(complaintService.getByCustomer(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getById(id));
    }
}
