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
    @Pattern(regexp = "^(QUEUED|SENT|FAILED)$", message = "status must be QUEUED, SENT or FAILED")
    private String status;
}
