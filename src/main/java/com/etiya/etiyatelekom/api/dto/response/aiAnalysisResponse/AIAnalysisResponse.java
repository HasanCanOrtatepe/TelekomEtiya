package com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse;

import com.etiya.etiyatelekom.common.enums.TicketPriorityEnums;
import com.etiya.etiyatelekom.common.enums.TicketRiskLevelEnums;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIAnalysisResponse {

    private Long id;
    private Long complaintId;

    private String summary;

    private TicketPriorityEnums priority;

    private TicketRiskLevelEnums riskLevel;

    private Float confidenceScore;

    private OffsetDateTime createdAt;

    private Long serviceDomainId;
    private Long departmentId;
}
