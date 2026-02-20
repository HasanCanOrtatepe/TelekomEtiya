package com.etiya.etiyatelekom.api.dto.response.ticketResponse;

import com.etiya.etiyatelekom.common.enums.TicketPriorityEnums;
import com.etiya.etiyatelekom.common.enums.TicketRiskLevelEnums;
import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;
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

    private TicketStatusEnums status;
    private TicketPriorityEnums priority;

    private TicketRiskLevelEnums riskLevel;

    private OffsetDateTime slaDueAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime closedAt;
}
