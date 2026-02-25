package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    List<Department> findByIsActiveTrue();
    boolean existsByNameIgnoreCase(String name);
    Optional<Department> findByIdAndIsActiveTrue(Long id);

}
