package com.etiya.etiyatelekom.api.dto.request.serviceDomainRequest;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDomainUpdateRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotNull
    private Boolean isActive;
}
