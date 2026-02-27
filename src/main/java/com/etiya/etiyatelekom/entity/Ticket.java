package com.etiya.etiyatelekom.entity;

import com.etiya.etiyatelekom.common.enums.TicketPriorityEnums;
import com.etiya.etiyatelekom.common.enums.TicketRiskLevelEnums;
import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "complaint_id", unique = true)
    private Complaint complaint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_domain_id")
    private ServiceDomain serviceDomain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_agent_id")
    private Agent assignedAgent;

    @Enumerated(EnumType.STRING)
    private TicketStatusEnums status;

    @Enumerated(EnumType.STRING)
    private TicketPriorityEnums priority;

    @Enumerated(EnumType.STRING)
    private TicketRiskLevelEnums riskLevel;

    private OffsetDateTime slaDueAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime closedAt;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketStatusHistory> statusHistories;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<EmailNotification> emailNotifications;
}