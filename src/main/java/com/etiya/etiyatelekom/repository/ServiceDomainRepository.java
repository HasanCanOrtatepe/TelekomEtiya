package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.ServiceDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceDomainRepository extends JpaRepository<ServiceDomain,Long> {

    boolean existsByNameIgnoreCase(String name);
    List<ServiceDomain> findByIsActiveTrue();
    Optional<ServiceDomain> findByIdAndIsActiveTrue(Long id);

}
