package com.etiya.etiyatelekom.api.dto.response.complaintResponse;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintResponse {

    private Long id;

    private Long customerId;

    private String title;
    private String description;

    private OffsetDateTime createdAt;

    private Long aiAnalysisId;
    private Long ticketId;
    private Long departmentId;
    private Long serviceDomainId;
    /** İçerik şikayet olarak kabul edilmediyse kullanıcıya gösterilecek mesaj */
    private String validationMessage;
}
