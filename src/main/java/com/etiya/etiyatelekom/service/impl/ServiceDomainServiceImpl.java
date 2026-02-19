package com.etiya.etiyatelekom.service.impl;

import com.etiya.etiyatelekom.api.dto.request.serviceDomainRequest.ServiceDomainCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.serviceDomainRequest.ServiceDomainUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse.ServiceDomainListResponse;
import com.etiya.etiyatelekom.api.dto.response.serviceDomainResponse.ServiceDomainResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceAlreadyExistsException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.ServiceDomain;
import com.etiya.etiyatelekom.repository.ServiceDomainRepository;
import com.etiya.etiyatelekom.service.abst.ServiceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceDomainServiceImpl implements ServiceDomainService {

    private final ServiceDomainRepository serviceDomainRepository;
    private final ModelMapperService modelMapperService;


    @Override
    public ServiceDomainResponse create(ServiceDomainCreateRequest request) {
        if (serviceDomainRepository.existsByNameIgnoreCase(request.getName())){
            throw new ResourceAlreadyExistsException("ServiceDomain","Name", request.getName());
        }
        ServiceDomain serviceDomain=modelMapperService.forRequest().map(request,ServiceDomain.class);
        serviceDomain.setIsActive(true);
        serviceDomainRepository.save(serviceDomain);

        ServiceDomainResponse serviceDomainResponse=modelMapperService.forResponse().map(serviceDomain,ServiceDomainResponse.class);

        return serviceDomainResponse;
    }

    @Override
    public ServiceDomainResponse update(Long id, ServiceDomainUpdateRequest request) {
        ServiceDomain serviceDomain=serviceDomainRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Service Domain","Id",id));

        serviceDomain.setName(request.getName());
        serviceDomainRepository.save(serviceDomain);

        ServiceDomainResponse serviceDomainResponse=modelMapperService.forResponse()
                .map(serviceDomain,ServiceDomainResponse.class);

        return serviceDomainResponse;
    }

    @Override
    public ServiceDomainResponse getById(Long id) {
        ServiceDomain serviceDomain=serviceDomainRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Service Domain","Id",id));

        ServiceDomainResponse serviceDomainResponse=modelMapperService.forResponse()
                .map(serviceDomain,ServiceDomainResponse.class);

        return serviceDomainResponse;
    }

    @Override
    public ServiceDomainListResponse getAll() {
        if (serviceDomainRepository.findAll().isEmpty()){
            throw new ResourceNotFoundException();
        }
        List<ServiceDomain> serviceDomains=serviceDomainRepository.findAll();
        List<ServiceDomainResponse> serviceDomainResponses= serviceDomains.stream()
                .map(serviceDomain -> modelMapperService.forResponse().map(serviceDomain,ServiceDomainResponse.class))
                .toList();

        ServiceDomainListResponse serviceDomainListResponse=ServiceDomainListResponse.builder()
                .items(serviceDomainResponses)
                .count(serviceDomainResponses.size())
                .build();

        return serviceDomainListResponse;
    }

    @Override
    public ServiceDomainListResponse getActive() {
        if (serviceDomainRepository.findByIsActiveTrue().isEmpty()){
            throw new ResourceNotFoundException();
        }

        List<ServiceDomain> serviceDomains=serviceDomainRepository.findByIsActiveTrue();
        List<ServiceDomainResponse> serviceDomainResponses= serviceDomains.stream()
                .map(serviceDomain -> modelMapperService.forResponse().map(serviceDomain,ServiceDomainResponse.class))
                .toList();

        ServiceDomainListResponse serviceDomainListResponse=ServiceDomainListResponse.builder()
                .items(serviceDomainResponses)
                .count(serviceDomainResponses.size())
                .build();

        return serviceDomainListResponse;
    }

    @Override
    public void deactivate(Long id) {
        ServiceDomain serviceDomain=serviceDomainRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Service Domain","Id",id));
        serviceDomain.setIsActive(false);
        serviceDomainRepository.save(serviceDomain);

    }

    @Override
    public void activate(Long id) {
        ServiceDomain serviceDomain=serviceDomainRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Service Domain","Id",id));
        serviceDomain.setIsActive(true);
        serviceDomainRepository.save(serviceDomain);
    }
}
