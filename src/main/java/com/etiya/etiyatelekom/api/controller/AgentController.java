package com.etiya.etiyatelekom.api.controller;

import com.etiya.etiyatelekom.api.dto.request.agentRequest.AgentCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.agentRequest.AgentUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.agentResponse.AgentListResponse;
import com.etiya.etiyatelekom.api.dto.response.agentResponse.AgentResponse;
import com.etiya.etiyatelekom.service.abst.AgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentService agentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AgentResponse> create(@Valid @RequestBody AgentCreateRequest request) {

        AgentResponse response = agentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AgentResponse> update(@PathVariable Long id, @Valid @RequestBody AgentUpdateRequest request) {

        return ResponseEntity.ok(agentService.update(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
    public ResponseEntity<AgentResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(agentService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
    public ResponseEntity<AgentListResponse> getAll() {

        return ResponseEntity.ok(agentService.getAll());
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
    public ResponseEntity<AgentListResponse> searchByName(@PathVariable String name) {

        return ResponseEntity.ok(agentService.searchByName(name));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
    public ResponseEntity<AgentListResponse> getByDepartment(@PathVariable Long departmentId) {

        return ResponseEntity.ok(agentService.getByDepartment(departmentId));
    }

    @GetMapping("/service-domain/{serviceDomainId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
    public ResponseEntity<AgentListResponse> getByServiceDomain(@PathVariable Long serviceDomainId) {

        return ResponseEntity.ok(agentService.getByServiceDomain(serviceDomainId));
    }

    @GetMapping("/available/{departmentId}/{serviceDomainId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
    public ResponseEntity<AgentListResponse> getAvailable(@PathVariable Long departmentId, @PathVariable Long serviceDomainId) {

        return ResponseEntity.ok(agentService.getAvailableAgents(departmentId, serviceDomainId));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        agentService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        agentService.activate(id);
        return ResponseEntity.noContent().build();
    }
}