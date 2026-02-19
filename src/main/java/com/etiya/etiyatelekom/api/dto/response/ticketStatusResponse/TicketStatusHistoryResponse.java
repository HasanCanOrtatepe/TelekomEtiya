package com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse;

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

    private String fromStatus;
    private String toStatus;

    private OffsetDateTime changedAt;
    private Long AgentId;
}
