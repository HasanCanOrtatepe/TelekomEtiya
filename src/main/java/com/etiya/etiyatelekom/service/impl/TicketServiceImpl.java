package com.etiya.etiyatelekom.service.impl;

import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketAssignRequest;
import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketUpdateRoutingRequest;
import com.etiya.etiyatelekom.api.dto.request.ticketRequest.TicketUpdateStatusRequest;
import com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse.AIAnalysisResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketResponse.TicketListResponse;
import com.etiya.etiyatelekom.api.dto.response.ticketResponse.TicketResponse;
import com.etiya.etiyatelekom.common.enums.TicketPriorityEnums;
import com.etiya.etiyatelekom.common.enums.TicketRiskLevelEnums;
import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;
import com.etiya.etiyatelekom.common.exception.exceptions.InvalidTicketStatusTransitionException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.*;
import com.etiya.etiyatelekom.repository.*;
import com.etiya.etiyatelekom.service.abst.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final DepartmentService departmentService;
    private final ServiceDomainService serviceDomainService;
    private final AgentService agentService;
    private final ModelMapperService modelMapperService;
    private final ComplaintRepository complaintRepository;
    private final TicketStatusHistoryService ticketStatusHistoryService;
    private final EmailNotificationService emailNotificationService;

    @Override
    public TicketResponse getById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "Id", id));

        return modelMapperService.forResponse().map(ticket, TicketResponse.class);
    }

    @Override
    public TicketListResponse getAll() {
        List<Ticket> tickets = ticketRepository.findAll();

        List<TicketResponse> items = tickets.stream()
                .map(t -> modelMapperService.forResponse().map(t, TicketResponse.class))
                .toList();

        return TicketListResponse.builder()
                .items(items)
                .count(items.size())
                .build();
    }

    @Override
    public TicketListResponse getByStatus(TicketStatusEnums status) {

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

        Agent agent = agentService.getActiveEntityById(request.getAgentId());
        ticket.setAssignedAgent(agent);

        Ticket saved = changeStatus(ticket, TicketStatusEnums.ASSIGNED);

        return modelMapperService.forResponse().map(saved, TicketResponse.class);
    }


    @Override
    public TicketResponse updateStatus(Long ticketId, TicketUpdateStatusRequest request) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", ticketId));

        Ticket saved = changeStatus(ticket, request.getStatus());

        return modelMapperService.forResponse().map(saved, TicketResponse.class);
    }

    @Override
    public TicketResponse updateRouting(Long ticketId, TicketUpdateRoutingRequest request) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", ticketId));

        if (request.getDepartmentId() != null) {
            Department department = departmentService.getActiveEntityById(request.getDepartmentId());
            ticket.setDepartment(department);
        }

        if (request.getServiceDomainId() != null) {
            ServiceDomain domain = serviceDomainService.getActiveEntityById(request.getServiceDomainId());
            ticket.setServiceDomain(domain);
        }

        Ticket saved = ticketRepository.save(ticket);
        return modelMapperService.forResponse().map(saved, TicketResponse.class);
    }

    @Override
    public TicketResponse close(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", ticketId));

        if (ticket.getStatus() == TicketStatusEnums.CLOSED) {
            return modelMapperService.forResponse().map(ticket, TicketResponse.class);
        }

        Ticket saved = changeStatus(ticket, TicketStatusEnums.CLOSED);

        return modelMapperService.forResponse().map(saved, TicketResponse.class);
    }

    @Override
    public TicketListResponse getSlaBreached() {

        OffsetDateTime now = OffsetDateTime.now();

        List<Ticket> tickets = ticketRepository.findBySlaDueAtBeforeAndStatusNot(now, TicketStatusEnums.CLOSED);

        List<TicketResponse> items = tickets.stream()
                .map(t -> modelMapperService.forResponse().map(t, TicketResponse.class))
                .toList();

        return TicketListResponse.builder()
                .items(items)
                .count(items.size())
                .build();
    }

    @Override
    public Ticket create(AIAnalysisResponse aiAnalysisResponse) {

        if (aiAnalysisResponse == null) {
            throw new ResourceNotFoundException();
        }

        Department department = departmentService.getActiveEntityById(aiAnalysisResponse.getDepartmentId());
        ServiceDomain serviceDomain = serviceDomainService.getActiveEntityById(aiAnalysisResponse.getServiceDomainId());

        Complaint complaint = complaintRepository.findById(aiAnalysisResponse.getComplaintId())
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", "Id", aiAnalysisResponse.getComplaintId()));

        Ticket ticket = Ticket.builder()
                .createdAt(OffsetDateTime.now())
                .priority(aiAnalysisResponse.getPriority())
                .complaint(complaint)
                .slaDueAt(OffsetDateTime.now().plusHours(department.getSlaHours()))
                .department(department)
                .status(null) // <<< önemli: null bırak
                .riskLevel(aiAnalysisResponse.getRiskLevel())
                .serviceDomain(serviceDomain)
                .build();

        Ticket saved = changeStatus(ticket, TicketStatusEnums.CREATED);

        if (saved.getRiskLevel() == TicketRiskLevelEnums.HIGH && saved.getPriority() == TicketPriorityEnums.CRITICAL) {
            emailNotificationService.sendMail(saved);
        }

        return saved;
    }

    private void changeTicket(Ticket ticket, TicketStatusEnums fromStatus, TicketStatusEnums toStatus) {

        Long agentId = null;
        Agent agent = ticket.getAssignedAgent();
        if (agent != null) {
            agentId = agent.getId();
        }

        TicketStatusHistory history = TicketStatusHistory.builder()
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .ticket(ticket)
                .agentId(agentId)
                .changedAt(OffsetDateTime.now())
                .build();

        ticketStatusHistoryService.create(history);
    }

    private Ticket changeStatus(Ticket ticket, TicketStatusEnums toStatus) {
        TicketStatusEnums fromStatus = ticket.getStatus();

        if (fromStatus == toStatus) {
            return ticket;
        }

        if (fromStatus != null && !fromStatus.canTransitionTo(toStatus)) {
            throw new InvalidTicketStatusTransitionException(fromStatus, toStatus);
        }

        ticket.setStatus(toStatus);

        if (toStatus == TicketStatusEnums.CLOSED && ticket.getClosedAt() == null) {
            ticket.setClosedAt(OffsetDateTime.now());
        }

        if (ticket.getId() == null) {
            ticket = ticketRepository.save(ticket);
        }

        changeTicket(ticket, fromStatus, toStatus);

        return ticketRepository.save(ticket);
    }

}
