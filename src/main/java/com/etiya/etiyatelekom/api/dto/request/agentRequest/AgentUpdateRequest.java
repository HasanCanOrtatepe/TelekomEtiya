package com.etiya.etiyatelekom.api.dto.request.agentRequest;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentUpdateRequest {

    private Long departmentId;
    private Long serviceDomainId;

    @NotBlank
    @Size(max = 150)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Pattern(regexp = "^(ADMIN|AGENT)$", message = "role must be ADMIN or AGENT")
    private String role;

    @NotBlank
    @Pattern(regexp = "^(ACTIVE|PASSIVE)$", message = "status must be ACTIVE or PASSIVE")
    private String status;
}
