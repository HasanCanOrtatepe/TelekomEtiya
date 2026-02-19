package com.etiya.etiyatelekom.api.dto.request.aiAnalysisRequest;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIAnalysisUpdateRequest {

    @NotBlank
    @Size(max = 5000)
    private String summary;

    @NotBlank
    private String priority;

    @NotBlank
    private String riskLevel;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "1.0", inclusive = true)
    private Float confidenceScore;
}
