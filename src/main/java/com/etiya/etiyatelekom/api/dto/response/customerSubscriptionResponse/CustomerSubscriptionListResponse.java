package com.etiya.etiyatelekom.api.dto.response.customerSubscriptionResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSubscriptionListResponse {
    private List<CustomerSubscriptionResponse> items;
    private int count;
}