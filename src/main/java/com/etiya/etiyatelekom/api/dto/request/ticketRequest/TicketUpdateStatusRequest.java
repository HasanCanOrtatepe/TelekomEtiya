package com.etiya.etiyatelekom.api.dto.request.ticketRequest;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketUpdateStatusRequest {

    @NotBlank
    @Pattern(
        regexp = "^(OPEN|IN_PROGRESS|ESCALATED|RESOLVED|CLOSED)$",
        message = "status must be OPEN, IN_PROGRESS, ESCALATED, RESOLVED or CLOSED"
    )
    private String status;
}
