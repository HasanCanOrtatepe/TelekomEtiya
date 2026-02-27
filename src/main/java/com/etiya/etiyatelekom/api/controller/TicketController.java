package com.etiya.etiyatelekom.api.controller;

import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketAssignRequest;
import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketUpdateRoutingRequest;
import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketUpdateStatusRequest;
import com.etiya.etiyatelekom.api.dto.response.ticketResponse.TicketListResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketResponse.TicketResponse;
import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;
import com.etiya.etiyatelekom.business.abst.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
@PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','SENIOR_AGENT','AGENT')")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getById(id));
    }

    @GetMapping
    public ResponseEntity<TicketListResponse> getAll() {
        return ResponseEntity.ok(ticketService.getAll());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<TicketListResponse> getByStatus(@PathVariable TicketStatusEnums status) {
        return ResponseEntity.ok(ticketService.getByStatus(status));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<TicketListResponse> getByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(ticketService.getByDepartment(departmentId));
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<TicketListResponse> getByAgent(@PathVariable Long agentId) {
        return ResponseEntity.ok(ticketService.getByAgent(agentId));
    }

    @GetMapping("/sla-breached")
    public ResponseEntity<TicketListResponse> getSlaBreached() {
        return ResponseEntity.ok(ticketService.getSlaBreached());
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<TicketResponse> assign(@PathVariable Long id,
                                                 @Valid @RequestBody TicketAssignRequest request) {
        return ResponseEntity.ok(ticketService.assignAgent(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody TicketUpdateStatusRequest request) {
        return ResponseEntity.ok(ticketService.updateStatus(id, request));
    }

    @PatchMapping("/{id}/routing")
    public ResponseEntity<TicketResponse> updateRouting(@PathVariable Long id, @Valid @RequestBody TicketUpdateRoutingRequest request) {
        return ResponseEntity.ok(ticketService.updateRouting(id, request));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<TicketResponse> close(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.close(id));
    }
}
