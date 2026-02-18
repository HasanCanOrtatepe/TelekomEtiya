package com.etiya.etiyatelekom.api.dto.response.subscriptionResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionListResponse {

    private List<SubscriptionResponse> items;
    private Integer count;
}
