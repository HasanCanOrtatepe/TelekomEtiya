package com.etiya.etiyatelekom.service.abst;


import com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse.TicketStatusHistoryListResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse.TicketStatusHistoryResponse;
import com.etiya.etiyatelekom.entity.TicketStatusHistory;

public interface TicketStatusHistoryService {

    TicketStatusHistoryResponse getById(Long id);

    TicketStatusHistoryListResponse getByTicket(Long ticketId);

    TicketStatusHistoryListResponse getByAgent(Long AgentId);

    TicketStatusHistoryListResponse getAll();

    TicketStatusHistory create(TicketStatusHistory ticketStatusHistory);
}
