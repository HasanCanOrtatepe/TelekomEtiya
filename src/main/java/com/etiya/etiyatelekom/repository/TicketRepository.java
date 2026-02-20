package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;
import com.etiya.etiyatelekom.entity.Agent;
import com.etiya.etiyatelekom.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findByAssignedAgent_Id(Long agentId);
    List<Ticket> findByStatus(TicketStatusEnums ticketStatusEnums);
    List<Ticket> findByDepartment_Id(Long departmentId);
    boolean existsByStatus(TicketStatusEnums ticketStatusEnums);
    boolean existsByAssignedAgent_Id(Long agentId);
    boolean existsByDepartment_Id(Long departmentId);
    List<Ticket> findBySlaDueAtBeforeAndStatusNot(OffsetDateTime now, TicketStatusEnums ticketStatusEnums);



}
