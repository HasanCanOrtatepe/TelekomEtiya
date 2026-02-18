package com.etiya.etiyatelekom.api.dto.request.departmentRequest;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentCreateRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotNull
    @Min(1)
    @Max(720)
    private Integer slaHours;

    @NotNull
    private Boolean isActive;
}
