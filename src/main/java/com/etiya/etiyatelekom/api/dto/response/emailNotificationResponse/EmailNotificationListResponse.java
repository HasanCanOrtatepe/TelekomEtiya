package com.etiya.etiyatelekom.api.dto.response.emailNotificationResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotificationListResponse {

    private List<EmailNotificationResponse> items;
    private Integer count;
}
