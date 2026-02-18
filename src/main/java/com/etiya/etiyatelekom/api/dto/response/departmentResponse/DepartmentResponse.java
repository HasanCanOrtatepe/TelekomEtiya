package com.etiya.etiyatelekom.api.dto.response.departmentResponse;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {

    private Long id;
    private String name;
    private Integer slaHours;
    private Boolean isActive;
}
