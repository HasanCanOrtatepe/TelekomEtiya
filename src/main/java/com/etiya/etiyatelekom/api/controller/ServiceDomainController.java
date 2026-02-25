package com.etiya.etiyatelekom.api.controller;

import com.etiya.etiyatelekom.api.dto.request.serviceDomainRequest.ServiceDomainCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.serviceDomainRequest.ServiceDomainUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse.ServiceDomainListResponse;
import com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse.ServiceDomainResponse;
import com.etiya.etiyatelekom.service.abst.ServiceDomainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-domains")
public class ServiceDomainController {

    private final ServiceDomainService serviceDomainService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDomainResponse> create(@Valid @RequestBody ServiceDomainCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceDomainService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDomainResponse> update(@PathVariable Long id,
                                                        @Valid @RequestBody ServiceDomainUpdateRequest request) {
        return ResponseEntity.ok(serviceDomainService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
    public ResponseEntity<ServiceDomainResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceDomainService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
    public ResponseEntity<ServiceDomainListResponse> getAll() {
        return ResponseEntity.ok(serviceDomainService.getAll());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
    public ResponseEntity<ServiceDomainListResponse> getActive() {
        return ResponseEntity.ok(serviceDomainService.getActive());
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        serviceDomainService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        serviceDomainService.activate(id);
        return ResponseEntity.noContent().build();
    }
}
