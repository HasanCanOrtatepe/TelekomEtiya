package com.etiya.etiyatelekom.api.dto.response.ticketResponse;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponse {

    private Long id;
    private Long complaintId;

    private Long serviceDomainId;
    private Long departmentId;
    private Long assignedAgentId;

    private String status;
    private String priority;
    private String riskLevel;

    private OffsetDateTime slaDueAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime closedAt;
}
