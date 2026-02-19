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
    private String status;
}
