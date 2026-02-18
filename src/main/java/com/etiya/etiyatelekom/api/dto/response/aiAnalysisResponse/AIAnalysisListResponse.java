package com.etiya.etiyatelekom.api.dto.response.aiAnalysisResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIAnalysisListResponse {

    private List<AIAnalysisResponse> items;
    private Integer count;
}
