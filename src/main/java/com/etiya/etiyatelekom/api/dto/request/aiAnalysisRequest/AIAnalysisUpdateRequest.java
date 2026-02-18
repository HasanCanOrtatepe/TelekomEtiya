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
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|URGENT)$",
            message = "priority must be LOW, MEDIUM, HIGH or URGENT")
    private String priority;

    @NotBlank
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH)$",
            message = "riskLevel must be LOW, MEDIUM or HIGH")
    private String riskLevel;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "1.0", inclusive = true)
    private Float confidenceScore;
}
