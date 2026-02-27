package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint,Long> {

    List<Complaint> findByCustomerId(Long customerId);

    List<Complaint> findByCustomerIdAndIsActiveTrue(Long customerId);

    List<Complaint> findByIsActiveTrue();

    boolean existsById(Long id);
}
