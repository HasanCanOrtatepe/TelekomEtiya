package com.etiya.etiyatelekom.api.dto.request.ticketRequest;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketUpdateRoutingRequest {

    @NotNull
    private Long departmentId;

    @NotNull
    private Long serviceDomainId;
}
