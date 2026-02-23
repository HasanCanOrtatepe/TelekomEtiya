package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent,Long> {
    boolean existsByEmail(String email);
    boolean existsByFullName(String fullName);
    List<Agent> findByFullNameContainingIgnoreCase(String fullName);
    List<Agent> findByDepartmentId(Long departmentId);
    List<Agent> findByServiceDomainId(Long serviceDomainId);
    boolean existsByDepartmentId(Long departmentId);
    boolean existsByServiceDomainId(Long serviceDomainId);
    Optional<Agent> findByIdAndIsActiveTrue(Long id);






}
