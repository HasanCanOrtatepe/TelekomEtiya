package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.common.enums.AgentRoleEnums;
import com.etiya.etiyatelekom.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent,Long> {
    boolean existsByRole(AgentRoleEnums role);
    List<Agent> findByRoleNot(AgentRoleEnums role);
    Optional<Agent> findByEmail(String email);
    Optional<Agent> findByEmailAndIsActiveTrue(String email);
    boolean existsByEmail(String email);
    List<Agent> findByFullNameContainingIgnoreCase(String fullName);
    List<Agent> findByDepartmentId(Long departmentId);
    List<Agent> findByServiceDomainId(Long serviceDomainId);
    Optional<Agent> findByIdAndIsActiveTrue(Long id);






}
