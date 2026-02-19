package com.etiya.etiyatelekom.api.controller;

import com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse.TicketStatusHistoryListResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse.TicketStatusHistoryResponse;
import com.etiya.etiyatelekom.service.abst.TicketStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticketStatusHistorys")
public class TicketStatusHistoryController {

    private final TicketStatusHistoryService ticketStatusHistoryService;

    @GetMapping
    public ResponseEntity<TicketStatusHistoryListResponse> getAll(){

        return ResponseEntity.ok(ticketStatusHistoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketStatusHistoryResponse> getById(@PathVariable Long id){

        return ResponseEntity.ok(ticketStatusHistoryService.getById(id));
    }

    @GetMapping("/Ticket/{id}")
    public ResponseEntity<TicketStatusHistoryListResponse> getByTicketId(@PathVariable Long id){

        return ResponseEntity.ok(ticketStatusHistoryService.getByTicket(id));
    }

    @GetMapping("/Agent/{id}")
    public ResponseEntity<TicketStatusHistoryListResponse> getByAgentId(@PathVariable Long id){

        return ResponseEntity.ok(ticketStatusHistoryService.getByAgent(id));
    }



}
