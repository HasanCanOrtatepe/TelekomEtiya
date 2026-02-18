package com.etiya.etiyatelekom.api.dto.response.subscriptionResponse;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponse {

    private Long id;
    private Long customerId;

    private String serviceType;
    private String packageName;
    private String status;

    private LocalDate activationDate;
}
