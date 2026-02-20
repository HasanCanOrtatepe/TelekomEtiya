package com.etiya.etiyatelekom.api.dto.request.ticketRequest;

import com.etiya.etiyatelekom.common.enums.TicketStatusEnums;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketUpdateStatusRequest {

    @NotNull
    private TicketStatusEnums status;
}
