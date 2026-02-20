package com.etiya.etiyatelekom.api.dto.request.subscriptionRequest;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionCreateRequest {

    @NotNull()
    @Positive()
    private Long serviceDomainId;

    @NotBlank
    @Size(max = 120)
    private String packageName;

    @NotNull
    private Double price;

    @NotNull()
    @Positive()
    private Integer durationDays;
}
