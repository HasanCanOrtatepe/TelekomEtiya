package com.etiya.etiyatelekom.business.abst;

import com.etiya.etiyatelekom.api.dto.request.agentRequest.AgentCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.agentRequest.AgentUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.agentResponse.AgentListResponse;
import com.etiya.etiyatelekom.api.dto.response.agentResponse.AgentResponse;
import com.etiya.etiyatelekom.entity.Agent;


public interface AgentService {

    AgentResponse create(AgentCreateRequest request);

    AgentResponse update(Long id, AgentUpdateRequest request);

    AgentResponse getById(Long id);

    AgentListResponse getAll();

    AgentListResponse searchByName(String name);

    AgentListResponse getByDepartment(Long departmentId);

    AgentListResponse getByServiceDomain(Long serviceDomainId);

    AgentListResponse getAvailableAgents(Long departmentId, Long serviceDomainId);

    AgentListResponse getNonAdminAgents();

    void deactivate(Long id);

    void activate(Long id);

    Agent getActiveEntityById(Long id);
}
