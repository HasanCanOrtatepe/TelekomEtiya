package com.etiya.etiyatelekom.api.dto.request.aiAnalysisRequest;

import com.etiya.etiyatelekom.common.enums.TicketPriorityEnums;
import com.etiya.etiyatelekom.common.enums.TicketRiskLevelEnums;
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

    @NotNull
    private TicketPriorityEnums priority;

    @NotNull
    private TicketRiskLevelEnums riskLevel;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "1.0", inclusive = true)
    private Float confidenceScore;
}
