package com.etiya.etiyatelekom.service.impl;


import com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse.TicketStatusHistoryListResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse.TicketStatusHistoryResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.TicketStatusHistory;
import com.etiya.etiyatelekom.repository.TicketStatusHistoryRepository;
import com.etiya.etiyatelekom.service.abst.TicketStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketStatusHistoryServiceImpl implements TicketStatusHistoryService {
    private final TicketStatusHistoryRepository ticketStatusHistoryRepository;
    private final ModelMapperService modelMapperService;


    @Override
    public TicketStatusHistoryResponse getById(Long id) {
        TicketStatusHistory ticketStatusHistory=ticketStatusHistoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("TicketStatusHistory","Id",id));
        TicketStatusHistoryResponse ticketStatusHistoryResponse=modelMapperService.forResponse().map(ticketStatusHistory,TicketStatusHistoryResponse.class);
        return ticketStatusHistoryResponse;
    }

    @Override
    public TicketStatusHistoryListResponse getByTicket(Long ticketId) {

        List<TicketStatusHistory> ticketStatusHistories=ticketStatusHistoryRepository.findByTicketId(ticketId);
        List<TicketStatusHistoryResponse> ticketStatusHistoryResponses=ticketStatusHistories.stream()
                .map(ticketStatusHistory -> TicketStatusHistoryResponse.builder()
                        .changedAt(ticketStatusHistory.getChangedAt())
                        .fromStatus(ticketStatusHistory.getFromStatus())
                        .ticketId(ticketStatusHistory.getTicket().getId())
                        .id(ticketStatusHistory.getId())
                        .toStatus(ticketStatusHistory.getToStatus())
                        .AgentId(ticketStatusHistory.getAgentId())
                        .build())
                .toList();

        TicketStatusHistoryListResponse ticketStatusHistoryListResponse=TicketStatusHistoryListResponse.builder()
                .items(ticketStatusHistoryResponses)
                .count(ticketStatusHistoryResponses.size())
                .build();

        return ticketStatusHistoryListResponse;
    }

    @Override
    public TicketStatusHistoryListResponse getByAgent(Long AgentId) {

        List<TicketStatusHistory> ticketStatusHistories=ticketStatusHistoryRepository.findByAgentId(AgentId);
        List<TicketStatusHistoryResponse> ticketStatusHistoryResponses=ticketStatusHistories.stream()
                .map(ticketStatusHistory -> TicketStatusHistoryResponse.builder()
                        .changedAt(ticketStatusHistory.getChangedAt())
                        .fromStatus(ticketStatusHistory.getFromStatus())
                        .ticketId(ticketStatusHistory.getTicket().getId())
                        .id(ticketStatusHistory.getId())
                        .toStatus(ticketStatusHistory.getToStatus())
                        .AgentId(ticketStatusHistory.getAgentId())
                        .build())
                .toList();

        TicketStatusHistoryListResponse ticketStatusHistoryListResponse=TicketStatusHistoryListResponse.builder()
                .items(ticketStatusHistoryResponses)
                .count(ticketStatusHistoryResponses.size())
                .build();

        return ticketStatusHistoryListResponse;
    }

    @Override
    public TicketStatusHistoryListResponse getAll() {

        List<TicketStatusHistory> ticketStatusHistories=ticketStatusHistoryRepository.findAll();
        List<TicketStatusHistoryResponse> ticketStatusHistoryResponses=ticketStatusHistories.stream()
                .map(ticketStatusHistory -> TicketStatusHistoryResponse.builder()
                        .changedAt(ticketStatusHistory.getChangedAt())
                        .fromStatus(ticketStatusHistory.getFromStatus())
                        .ticketId(ticketStatusHistory.getTicket().getId())
                        .id(ticketStatusHistory.getId())
                        .toStatus(ticketStatusHistory.getToStatus())
                        .AgentId(ticketStatusHistory.getAgentId())
                        .build())
                .toList();

        TicketStatusHistoryListResponse ticketStatusHistoryListResponse=TicketStatusHistoryListResponse.builder()
                .items(ticketStatusHistoryResponses)
                .count(ticketStatusHistoryResponses.size())
                .build();

        return ticketStatusHistoryListResponse;
    }

    @Override
    public TicketStatusHistory create(TicketStatusHistory history) {
        return ticketStatusHistoryRepository.save(history);
    }
}
