package com.etiya.etiyatelekom.service.abst;

import com.etiya.etiyatelekom.api.dto.request.agentRequest.AgentCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.agentRequest.AgentUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.agentResponse.AgentListResponse;
import com.etiya.etiyatelekom.api.dto.response.agentResponse.AgentResponse;


public interface AgentService {

    AgentResponse create(AgentCreateRequest request);

    AgentResponse update(Long id, AgentUpdateRequest request);

    AgentResponse getById(Long id);

    AgentListResponse getAll();

    AgentListResponse searchByName(String name);

    AgentListResponse getByDepartment(Long departmentId);

    AgentListResponse getByServiceDomain(Long serviceDomainId);

    AgentListResponse getAvailableAgents(Long departmentId, Long serviceDomainId);

    void deactivate(Long id);
}
