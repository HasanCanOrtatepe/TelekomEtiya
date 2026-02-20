package com.etiya.etiyatelekom.api.dto.request.customerSubscriptionRequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSubscriptionPurchaseRequest {

    @NotNull(message = "customerId is required")
    @Positive(message = "customerId must be positive")
    private Long customerId;

    @NotNull(message = "subscriptionId is required")
    @Positive(message = "subscriptionId must be positive")
    private Long subscriptionId;
}