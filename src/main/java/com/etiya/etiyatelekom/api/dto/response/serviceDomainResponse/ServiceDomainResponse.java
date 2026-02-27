package com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDomainResponse {

    private Long id;
    private String name;
    private Boolean isActive;

    @Override
    public String toString() {
        return "id=" + id + ", name='" + name ;
    }
}
