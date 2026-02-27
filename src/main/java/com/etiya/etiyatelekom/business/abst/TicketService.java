package com.etiya.etiyatelekom.business.abst;


import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketAssignRequest;
import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketUpdateRoutingRequest;
import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketUpdateStatusRequest;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketResponse.TicketListResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketResponse.TicketResponse;
import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;
import com.etiya.etiyatelekom.entity.Ticket;

public interface TicketService {

    TicketResponse getById(Long id);

    TicketListResponse getAll();

    TicketListResponse getByStatus(TicketStatusEnums request);

    TicketListResponse getByDepartment(Long departmentId);

    TicketListResponse getByAgent(Long agentId);

    TicketResponse assignAgent(Long ticketId, TicketAssignRequest request);

    TicketResponse updateStatus(Long ticketId, TicketUpdateStatusRequest request);

    TicketResponse updateRouting(Long ticketId, TicketUpdateRoutingRequest request);

    TicketResponse close(Long ticketId);

    TicketListResponse getSlaBreached();

    Ticket create(AIAnalysisResponse aiAnalysisResponse);
}

