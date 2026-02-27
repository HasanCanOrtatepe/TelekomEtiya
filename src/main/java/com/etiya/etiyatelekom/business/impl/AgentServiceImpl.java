package com.etiya.etiyatelekom.business.impl;

import com.etiya.etiyatelekom.api.dto.request.agentRequest.AgentCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.agentRequest.AgentUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.agentResponse.AgentListResponse;
import com.etiya.etiyatelekom.api.dto.response.agentResponse.AgentResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.CannotDeactivateAdminException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceAlreadyExistsException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.common.enums.AgentRoleEnums;
import com.etiya.etiyatelekom.entity.Agent;
import com.etiya.etiyatelekom.repository.AgentRepository;
import com.etiya.etiyatelekom.repository.CustomerRepository;
import com.etiya.etiyatelekom.business.abst.AgentService;
import com.etiya.etiyatelekom.business.abst.DepartmentService;
import com.etiya.etiyatelekom.business.abst.ServiceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final CustomerRepository customerRepository;
    private final DepartmentService departmentService;
    private final ServiceDomainService serviceDomainService;
    private final ModelMapperService modelMapperService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AgentResponse create(AgentCreateRequest request) {
        if (agentRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Agent", "Email", request.getEmail());
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Customer", "Email", request.getEmail());
        }

        Agent agent=Agent.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        agent.setIsActive(true);


        agent.setDepartment(departmentService.getActiveEntityById(request.getDepartmentId()));
        agent.setServiceDomain(serviceDomainService.getActiveEntityById(request.getServiceDomainId()));
        agent.setPassword(passwordEncoder.encode(request.getPassword()));

        agentRepository.save(agent);

        AgentResponse agentResponse=modelMapperService.forResponse().map(agent,AgentResponse.class);

        return agentResponse;
    }

    @Override
    public AgentResponse update(Long id, AgentUpdateRequest request) {

        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", id));

        if (request.getDepartmentId()!=null){
            agent.setDepartment(departmentService.getActiveEntityById(request.getDepartmentId()));

        }

        if (request.getServiceDomainId()!=null){
            agent.setServiceDomain(serviceDomainService.getActiveEntityById(request.getServiceDomainId()));
        }
        if (!agent.getEmail().equalsIgnoreCase(request.getEmail())) {
            if (agentRepository.existsByEmail(request.getEmail())) {
                throw new ResourceAlreadyExistsException("Agent", "Email", request.getEmail());
            }
            if (customerRepository.existsByEmail(request.getEmail())) {
                throw new ResourceAlreadyExistsException("Customer", "Email", request.getEmail());
            }
        }
        agent.setFullName(request.getFullName());
        agent.setEmail(request.getEmail());
        agent.setRole(request.getRole());
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

        List<Agent> agents=agentRepository.findByDepartmentId(departmentId)
                .stream().filter(agent -> agent.getServiceDomain().getId().equals(serviceDomainId)).toList();

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
    public AgentListResponse getNonAdminAgents() {
        List<Agent> agents = agentRepository.findByRoleNot(AgentRoleEnums.ADMIN);
        List<AgentResponse> agentResponseList = agents.stream()
                .map(agent -> modelMapperService.forResponse().map(agent, AgentResponse.class))
                .toList();
        return AgentListResponse.builder()
                .agents(agentResponseList)
                .count(agentResponseList.size())
                .build();
    }

    @Override
    public void deactivate(Long id) {
        Agent agent = agentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Agent", "Id", id));
        if (agent.getRole() == AgentRoleEnums.ADMIN) {
            throw new CannotDeactivateAdminException();
        }
        agent.setIsActive(false);
        agentRepository.save(agent);
    }

    @Override
    public void activate(Long id) {
        Agent agent=agentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Agent","Id",id));
        agent.setIsActive(true);
        agentRepository.save(agent);

    }

    @Override
    public Agent getActiveEntityById(Long id) {

        return agentRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Active Agent", "Id", id));
    }


}
