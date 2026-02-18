package com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDomainListResponse {

    private List<ServiceDomainResponse> items;
    private Integer count;
}
