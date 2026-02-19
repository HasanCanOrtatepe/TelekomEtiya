package com.etiya.etiyatelekom.api.dto.request.emailNotificationRequest;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotificationCreateRequest {

    @NotNull
    private Long ticketId;

    @NotBlank
    @Email
    @Size(max = 150)
    private String recipientEmail;

    @NotBlank
    private String status;
}
