package com.etiya.etiyatelekom.service.impl;

import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketAssignRequest;
import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketUpdateRoutingRequest;
import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketUpdateStatusRequest;
import com.etiya.etiyatelekom.api.dto.response.ticketResponse.TicketListResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketResponse.TicketResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.*;
import com.etiya.etiyatelekom.repository.*;
import com.etiya.etiyatelekom.service.abst.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final AgentRepository agentRepository;
    private final DepartmentRepository departmentRepository;
    private final ServiceDomainRepository serviceDomainRepository;
    private final ModelMapperService modelMapperService;
    private final TicketStatusHistoryRepository ticketStatusHistoryRepository;

    @Override
    public TicketResponse getById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "Id", id));

        return modelMapperService.forResponse().map(ticket, TicketResponse.class);
    }

    @Override
    public TicketListResponse getAll() {
        List<Ticket> tickets = ticketRepository.findAll();
        if (tickets.isEmpty()) {
            throw new ResourceNotFoundException("Ticket", "list", "empty");
        }

        List<TicketResponse> items = tickets.stream()
                .map(t -> modelMapperService.forResponse().map(t, TicketResponse.class))
                .toList();

        return TicketListResponse.builder()
                .items(items)
                .count(items.size())
                .build();
    }

    @Override
    public TicketListResponse getByStatus(String status) {

        if (!ticketRepository.existsByStatus(status)) {
            throw new ResourceNotFoundException("Ticket", "status", status);
        }

        List<TicketResponse> items = ticketRepository.findByStatus(status).stream()
                .map(t -> modelMapperService.forResponse().map(t, TicketResponse.class))
                .toList();

        return TicketListResponse.builder()
                .items(items)
                .count(items.size())
                .build();
    }

    @Override
    public TicketListResponse getByDepartment(Long departmentId) {

        if (!ticketRepository.existsByDepartment_Id(departmentId)) {
            throw new ResourceNotFoundException("Ticket", "departmentId", departmentId);
        }

        List<TicketResponse> items = ticketRepository.findByDepartment_Id(departmentId).stream()
                .map(t -> modelMapperService.forResponse().map(t, TicketResponse.class))
                .toList();

        return TicketListResponse.builder()
                .items(items)
                .count(items.size())
                .build();
    }

    @Override
    public TicketListResponse getByAgent(Long agentId) {

        if (!ticketRepository.existsByAssignedAgent_Id(agentId)) {
            throw new ResourceNotFoundException("Ticket", "agentId", agentId);
        }

        List<TicketResponse> items = ticketRepository.findByAssignedAgent_Id(agentId).stream()
                .map(t -> modelMapperService.forResponse().map(t, TicketResponse.class))
                .toList();

        return TicketListResponse.builder()
                .items(items)
                .count(items.size())
                .build();
    }

    @Override
    public TicketResponse assignAgent(Long ticketId, TicketAssignRequest request) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", ticketId));

        Agent agent = agentRepository.findById(request.getAgentId())
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", request.getAgentId()));

        ticket.setAssignedAgent(agent);

        Ticket saved = ticketRepository.save(ticket);
        return modelMapperService.forResponse().map(saved, TicketResponse.class);
    }

    @Override
    public TicketResponse updateStatus(Long ticketId, TicketUpdateStatusRequest request) {


        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", ticketId));

        changeticket(ticketId,ticket.getStatus(),request.getStatus());

        ticket.setStatus(request.getStatus());

        Ticket saved = ticketRepository.save(ticket);
        return modelMapperService.forResponse().map(saved, TicketResponse.class);
    }

    @Override
    public TicketResponse updateRouting(Long ticketId, TicketUpdateRoutingRequest request) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", ticketId));

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
            ticket.setDepartment(department);
        }

        if (request.getServiceDomainId() != null) {
            ServiceDomain domain = serviceDomainRepository.findById(request.getServiceDomainId())
                    .orElseThrow(() -> new ResourceNotFoundException("ServiceDomain", "id", request.getServiceDomainId()));
            ticket.setServiceDomain(domain);
        }

        Ticket saved = ticketRepository.save(ticket);
        return modelMapperService.forResponse().map(saved, TicketResponse.class);
    }

    @Override
    public TicketResponse close(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", ticketId));


        ticket.setStatus("CLOSED");
        ticket.setClosedAt(OffsetDateTime.now());

        Ticket saved = ticketRepository.save(ticket);
        changeticket(ticketId,ticket.getStatus(),"CLOSED");

        return modelMapperService.forResponse().map(saved, TicketResponse.class);
    }

    @Override
    public TicketListResponse getSlaBreached() {

        OffsetDateTime now = OffsetDateTime.now();

        List<Ticket> tickets = ticketRepository.findBySlaDueAtBeforeAndStatusNot(now, "CLOSED");
        if (tickets.isEmpty()) {
            throw new ResourceNotFoundException("Ticket", "slaBreached", "none");
        }

        List<TicketResponse> items = tickets.stream()
                .map(t -> modelMapperService.forResponse().map(t, TicketResponse.class))
                .toList();

        return TicketListResponse.builder()
                .items(items)
                .count(items.size())
                .build();
    }

    private void changeticket(Long id,String firstStatus,String secondStatus){

        log.info("1 changeticket");

        Ticket ticket=ticketRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Ticket","Id",id));

        log.info("2 changeticket");
        Long agentId=null;
        Agent agent=ticket.getAssignedAgent();
        if (agent!=null){
            agentId=agent.getId();
        }

        TicketStatusHistory ticketStatusHistory=TicketStatusHistory.builder()
                .fromStatus(firstStatus)
                .ticket(ticket)
                .changedAt(OffsetDateTime.now())
                .toStatus(secondStatus)
                .agentId(agentId)
                .build();
        log.info("3 changeticket");
        ticketStatusHistoryRepository.save(ticketStatusHistory);
        log.info("4 changeticket");
    }
}
