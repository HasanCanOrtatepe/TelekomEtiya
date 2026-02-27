package com.etiya.etiyatelekom.business.abst;

import com.etiya.etiyatelekom.api.dto.request.serviceDomainRequest.ServiceDomainCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.serviceDomainRequest.ServiceDomainUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse.ServiceDomainListResponse;
import com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse.ServiceDomainResponse;
import com.etiya.etiyatelekom.entity.ServiceDomain;


public interface ServiceDomainService {

    ServiceDomainResponse create(ServiceDomainCreateRequest request);

    ServiceDomainResponse update(Long id, ServiceDomainUpdateRequest request);

    ServiceDomainResponse getById(Long id);

    ServiceDomainListResponse getAll();

    ServiceDomainListResponse getActive();

    void deactivate(Long id);

    void activate(Long id);

    ServiceDomain getActiveEntityById(Long id);
}
