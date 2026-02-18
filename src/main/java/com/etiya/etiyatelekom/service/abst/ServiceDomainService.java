package com.etiya.etiyatelekom.service.abst;

import com.etiya.etiyatelekom.api.dto.request.serviceDomainRequest.ServiceDomainCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.serviceDomainRequest.ServiceDomainUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse.ServiceDomainListResponse;
import com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse.ServiceDomainResponse;


public interface ServiceDomainService {

    ServiceDomainResponse create(ServiceDomainCreateRequest request);

    ServiceDomainResponse update(Long id, ServiceDomainUpdateRequest request);

    ServiceDomainResponse getById(Long id);

    ServiceDomainListResponse getAll();

    ServiceDomainListResponse getActive();

    void deactivate(Long id);
}
