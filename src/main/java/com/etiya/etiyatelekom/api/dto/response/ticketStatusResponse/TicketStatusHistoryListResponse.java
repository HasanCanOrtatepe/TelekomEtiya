package com.etiya.etiyatelekom.api.dto.response.ticketStatusResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketStatusHistoryListResponse {

    private List<TicketStatusHistoryResponse> items;
    private Integer count;
}
