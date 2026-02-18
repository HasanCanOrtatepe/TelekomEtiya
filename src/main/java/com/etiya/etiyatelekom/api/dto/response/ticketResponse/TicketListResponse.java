package com.etiya.etiyatelekom.api.dto.response.ticketResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketListResponse {

    private List<TicketResponse> items;
    private Integer count;
}
