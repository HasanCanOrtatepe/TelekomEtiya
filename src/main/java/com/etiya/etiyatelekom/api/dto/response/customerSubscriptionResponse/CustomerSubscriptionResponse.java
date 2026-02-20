package com.etiya.etiyatelekom.api.dto.response.customerSubscriptionResponse;

import com.etiya.etiyatelekom.common.enums.SubscriptionStatus;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSubscriptionResponse {

    private Long id;

    private Long customerId;
    private Long subscriptionId;

    private String serviceType;
    private String packageName;

    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private SubscriptionStatus status;
}
