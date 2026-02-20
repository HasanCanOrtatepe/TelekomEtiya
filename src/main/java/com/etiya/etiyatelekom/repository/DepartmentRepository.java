package com.etiya.etiyatelekom.repository;

import com.etiya.etiyatelekom.entity.Agent;
import com.etiya.etiyatelekom.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    List<Department> findByIsActiveTrue();
    boolean existsByIdIsNotNull();
    boolean existsByNameIgnoreCase(String name);

}
