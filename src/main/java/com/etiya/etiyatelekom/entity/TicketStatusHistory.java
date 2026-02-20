package com.etiya.etiyatelekom.entity;

import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "ticket_status_history")
public class TicketStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private TicketStatusEnums fromStatus;

    private TicketStatusEnums toStatus;

    private OffsetDateTime changedAt;

    private Long agentId;

    @Enumerated(EnumType.STRING)
    private TicketStatusEnums status;
}