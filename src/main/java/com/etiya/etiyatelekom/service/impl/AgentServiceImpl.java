package com.etiya.etiyatelekom.service.impl;

import com.etiya.etiyatelekom.api.dto.request.agentRequest.AgentCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.agentRequest.AgentUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.agentResponse.AgentListResponse;
import com.etiya.etiyatelekom.api.dto.response.agentResponse.AgentResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceAlreadyExistsException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.Agent;
import com.etiya.etiyatelekom.repository.AgentRepository;
import com.etiya.etiyatelekom.repository.DepartmentRepository;
import com.etiya.etiyatelekom.repository.ServiceDomainRepository;
import com.etiya.etiyatelekom.service.abst.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final DepartmentRepository departmentRepository;
    private final ServiceDomainRepository serviceDomainRepository;
    private final ModelMapperService modelMapperService;



    @Override
    public AgentResponse create(AgentCreateRequest request) {

        Agent agent=modelMapperService.forRequest().map(request,Agent.class);

        if (request.getDepartmentId()!=null){
            agent.setDepartment(departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(()-> new ResourceNotFoundException("Department","Id",request.getDepartmentId())));
        }

        if (request.getServiceDomainId()!=null){
            agent.setServiceDomain(serviceDomainRepository.findById(request.getServiceDomainId())
                    .orElseThrow(()-> new ResourceNotFoundException("Service Domain","Id",request.getServiceDomainId())));
        }
        agentRepository.save(agent);

        return modelMapperService.forResponse().map(agent,AgentResponse.class);
    }

    @Override
    public AgentResponse update(Long id, AgentUpdateRequest request) {

        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", id));

        if (request.getDepartmentId()!=null){
            agent.setDepartment(departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(()-> new ResourceNotFoundException("Department","Id",request.getDepartmentId())));
        }

        if (request.getServiceDomainId()!=null){
            agent.setServiceDomain(serviceDomainRepository.findById(request.getServiceDomainId())
                    .orElseThrow(()-> new ResourceNotFoundException("Service Domain","Id",request.getServiceDomainId())));
        }
        if (agentRepository.existsByEmail(request.getEmail())){
            throw new ResourceAlreadyExistsException("Agent","Email",request.getEmail());
        }
        agent.setFullName(request.getFullName());
        agent.setEmail(request.getEmail());
        agent.setRole(request.getRole());
        agent.setStatus(request.getStatus());
        agentRepository.save(agent);

        return modelMapperService.forResponse().map(agent,AgentResponse.class);
    }

    @Override
    public AgentResponse getById(Long id) {
        Agent agent=agentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Agent","Id",id));
        return modelMapperService.forResponse().map(agent,AgentResponse.class);
    }

    @Override
    public AgentListResponse getAll() {
        if (agentRepository.findAll().isEmpty()){
            throw new ResourceNotFoundException();
        }
        List<Agent> agents=agentRepository.findAll();
        List<AgentResponse> agentResponseList=agents.stream()
                .map(agent -> modelMapperService.forResponse().map(agent,AgentResponse.class))
                .toList();

        AgentListResponse agentListResponse=AgentListResponse.builder()
                .agents(agentResponseList)
                .count(agentResponseList.size())
                .build();
        return agentListResponse;
    }

    @Override
    public AgentListResponse searchByName(String name) {
        if (!agentRepository.existsByFullName(name)){
            throw new ResourceNotFoundException();
        }
        List<Agent> agents=agentRepository.findByFullNameContainingIgnoreCase(name);

        List<AgentResponse> agentResponseList=agents.stream()
                .map(agent -> modelMapperService.forResponse().map(agent,AgentResponse.class))
                .toList();

        AgentListResponse agentListResponse=AgentListResponse.builder()
                .agents(agentResponseList)
                .count(agentResponseList.size())
                .build();
        return agentListResponse;
    }

    @Override
    public AgentListResponse getByDepartment(Long departmentId) {
        if (!agentRepository.existsByDepartmentId(departmentId)){
            throw new ResourceNotFoundException();
        }
        List<Agent> agents=agentRepository.findByDepartmentId(departmentId);

        List<AgentResponse> agentResponseList=agents.stream()
                .map(agent -> modelMapperService.forResponse().map(agent,AgentResponse.class))
                .toList();

        AgentListResponse agentListResponse=AgentListResponse.builder()
                .agents(agentResponseList)
                .count(agentResponseList.size())
                .build();
        return agentListResponse;
    }

    @Override
    public AgentListResponse getByServiceDomain(Long serviceDomainId) {
        if (!agentRepository.existsByServiceDomainId(serviceDomainId)) {
            throw new ResourceNotFoundException();
        }
        List<Agent> agents = agentRepository.findByServiceDomainId(serviceDomainId);

        List<AgentResponse> agentResponseList = agents.stream()
                .map(agent -> modelMapperService.forResponse().map(agent, AgentResponse.class))
                .toList();

        AgentListResponse agentListResponse = AgentListResponse.builder()
                .agents(agentResponseList)
                .count(agentResponseList.size())
                .build();
        return agentListResponse;
    }
    @Override
    public AgentListResponse getAvailableAgents(Long departmentId, Long serviceDomainId) {
        if (!agentRepository.existsByDepartmentId(departmentId)&&!agentRepository.existsByServiceDomainId(serviceDomainId)){
            throw new ResourceNotFoundException();
        }
        List<Agent> agents=agentRepository.findByDepartmentId(departmentId)
                .stream().filter(agent -> agent.getServiceDomain().equals(serviceDomainId)).toList();

        List<AgentResponse> agentResponseList = agents.stream()
                .map(agent -> modelMapperService.forResponse().map(agent, AgentResponse.class))
                .toList();

        AgentListResponse agentListResponse = AgentListResponse.builder()
                .agents(agentResponseList)
                .count(agentResponseList.size())
                .build();
        return agentListResponse;
    }

    @Override
    public void deactivate(Long id) {
        Agent agent=agentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException());
        agent.setStatus("Deactive");
        agentRepository.save(agent);

    }
}
