package com.etiya.etiyatelekom.api.dto.response.departmentResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentListResponse {

    private List<DepartmentResponse> items;
    private Integer count;
}
