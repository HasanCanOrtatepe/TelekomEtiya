package com.etiya.etiyatelekom.api.dto.request.customerSubscriptionRequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSubscriptionExtendRequest {

    @NotNull(message = "days is required")
    @Positive(message = "days must be positive")
    private Integer days;
}
