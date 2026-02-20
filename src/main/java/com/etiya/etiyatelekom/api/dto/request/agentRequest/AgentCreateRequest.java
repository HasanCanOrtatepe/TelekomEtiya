package com.etiya.etiyatelekom.api.dto.request.agentRequest;


import com.etiya.etiyatelekom.common.enums.AgentRoleEnums;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentCreateRequest {

    private Long departmentId;
    private Long serviceDomainId;

    @NotBlank
    @Size(max = 150)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotNull
    private AgentRoleEnums role;
}
