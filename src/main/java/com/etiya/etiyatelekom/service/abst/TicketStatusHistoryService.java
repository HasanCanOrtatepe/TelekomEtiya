package com.etiya.etiyatelekom.service.abst;


import com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse.TicketStatusHistoryListResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse.TicketStatusHistoryResponse;

public interface TicketStatusHistoryService {

    TicketStatusHistoryResponse getById(Long id);

    TicketStatusHistoryListResponse getByTicket(Long ticketId);

    TicketStatusHistoryListResponse getAll();
}
