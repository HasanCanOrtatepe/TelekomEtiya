package com.etiya.etiyatelekom.api.dto.response.emailNotificationResponse;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotificationResponse {

    private Long id;
    private Long ticketId;

    private String recipientEmail;
    private String status;

    private OffsetDateTime sentAt;
}
