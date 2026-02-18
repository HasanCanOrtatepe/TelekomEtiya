package com.etiya.etiyatelekom.api.dto.request.subscriptionRequest;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionUpdateRequest {

    @NotBlank
    @Size(max = 80)
    private String serviceType;

    @NotBlank
    @Size(max = 120)
    private String packageName;


}
