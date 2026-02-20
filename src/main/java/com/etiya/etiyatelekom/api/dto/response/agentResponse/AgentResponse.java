package com.etiya.etiyatelekom.api.dto.response.agentResponse;

import com.etiya.etiyatelekom.common.enums.AgentRoleEnums;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentResponse {

    private Long id;
    private Long departmentId;
    private Long serviceDomainId;

    private String fullName;
    private String email;
    private AgentRoleEnums role;
    private Boolean isActive;

}
