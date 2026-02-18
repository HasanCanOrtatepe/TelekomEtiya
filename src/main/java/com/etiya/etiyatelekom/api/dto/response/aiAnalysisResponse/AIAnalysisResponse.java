package com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIAnalysisResponse {

    private Long id;
    private Long complaintId;

    private String summary;
    private String priority;
    private String riskLevel;
    private Float confidenceScore;

    private OffsetDateTime createdAt;
}
