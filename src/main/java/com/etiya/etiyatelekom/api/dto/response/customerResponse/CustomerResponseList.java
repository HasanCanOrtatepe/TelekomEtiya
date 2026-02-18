package com.etiya.etiyatelekom.api.dto.response.customerResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseList {

    List<CustomerResponse> customerResponses;
    private Integer count;

}