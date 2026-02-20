package com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse;

import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TicketStatusHistoryResponse {

    private Long id;
    private Long ticketId;

    private TicketStatusEnums fromStatus;
    private TicketStatusEnums toStatus;

    private OffsetDateTime changedAt;
    private Long AgentId;
}
