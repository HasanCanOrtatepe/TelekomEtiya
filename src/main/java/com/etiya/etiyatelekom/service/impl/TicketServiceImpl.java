package com.etiya.etiyatelekom.service.impl;


import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketAssignRequest;
import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketUpdateRoutingRequest;
import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketUpdateStatusRequest;
import com.etiya.etiyatelekom.api.dto.response.ticketResponse.TicketListResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketResponse.TicketResponse;
import com.etiya.etiyatelekom.service.abst.TicketService;

public class TicketServiceImpl implements TicketService {


    @Override
    public TicketResponse getById(Long id) {
        return null;
    }

    @Override
    public TicketListResponse getAll() {
        return null;
    }

    @Override
    public TicketListResponse getByStatus(String status) {
        return null;
    }

    @Override
    public TicketListResponse getByDepartment(Long departmentId) {
        return null;
    }

    @Override
    public TicketListResponse getByAgent(Long agentId) {
        return null;
    }

    @Override
    public TicketResponse assignAgent(Long ticketId, TicketAssignRequest request) {
        return null;
    }

    @Override
    public TicketResponse updateStatus(Long ticketId, TicketUpdateStatusRequest request) {
        return null;
    }

    @Override
    public TicketResponse updateRouting(Long ticketId, TicketUpdateRoutingRequest request) {
        return null;
    }

    @Override
    public TicketResponse close(Long ticketId) {
        return null;
    }

    @Override
    public TicketListResponse getSlaBreached() {
        return null;
    }
}

