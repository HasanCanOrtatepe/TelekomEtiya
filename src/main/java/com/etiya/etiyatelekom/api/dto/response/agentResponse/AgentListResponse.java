package com.etiya.etiyatelekom.api.dto.response.agentResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentListResponse {

    private List<AgentResponse> agents;

    private Integer count;
}
