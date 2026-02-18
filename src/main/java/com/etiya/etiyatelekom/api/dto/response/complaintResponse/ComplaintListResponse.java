package com.etiya.etiyatelekom.api.dto.response.complaintResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintListResponse {

    private List<ComplaintResponse> items;
    private Integer count;
}
