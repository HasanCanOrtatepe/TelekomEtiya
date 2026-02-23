package com.etiya.etiyatelekom.service.impl;


import com.etiya.etiyatelekom.api.dto.request.departmentRequest.DepartmentCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.departmentRequest.DepartmentUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.departmentResponse.DepartmentListResponse;
import com.etiya.etiyatelekom.api.dto.response.departmentResponse.DepartmentResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceAlreadyExistsException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.Department;
import com.etiya.etiyatelekom.repository.DepartmentRepository;
import com.etiya.etiyatelekom.service.abst.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapperService modelMapperService;


    @Override
    public DepartmentResponse create(DepartmentCreateRequest request) {

        if (departmentRepository.existsByNameIgnoreCase(request.getName())){
            throw new ResourceAlreadyExistsException("Department","Name",request.getName());
        }

        Department department=modelMapperService.forRequest().map(request,Department.class);
        department.setIsActive(true);
        departmentRepository.save(department);

        return modelMapperService.forResponse().map(department,DepartmentResponse.class);

    }

    @Override
    public DepartmentResponse update(Long id, DepartmentUpdateRequest request) {

        Department department=departmentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Department","Id",id));

        department.setName(request.getName());
        department.setSlaHours(request.getSlaHours());
        departmentRepository.save(department);
        DepartmentResponse departmentResponse=modelMapperService.forResponse().map(department,DepartmentResponse.class);

        return departmentResponse;
    }

    @Override
    public DepartmentResponse getById(Long id) {
        Department department=departmentRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        DepartmentResponse departmentResponse=modelMapperService.forResponse().map(department,DepartmentResponse.class);

        return departmentResponse;
    }

    @Override
    public DepartmentListResponse getAll() {

        List<Department> departments=departmentRepository.findAll();
        List<DepartmentResponse> departmentResponses= departments.stream()
                .map(department -> modelMapperService.forResponse().map(department,DepartmentResponse.class))
                .toList();
        DepartmentListResponse departmentListResponse=DepartmentListResponse.builder()
                .items(departmentResponses)
                .count(departmentResponses.size())
                .build();
        return departmentListResponse;
    }

    @Override
    public DepartmentListResponse getActive() {

        List<Department> departments= departmentRepository.findByIsActiveTrue();
        List<DepartmentResponse> departmentResponses= departments.stream()
                .map(department -> modelMapperService.forResponse().map(department,DepartmentResponse.class))
                .toList();
        DepartmentListResponse departmentListResponse=DepartmentListResponse.builder()
                .items(departmentResponses)
                .count(departmentResponses.size())
                .build();
        return departmentListResponse;

    }

    @Override
    public void deactivate(Long id) {
        Department department=departmentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Department","Id",id));
        department.setIsActive(false);
        departmentRepository.save(department);

    }

    @Override
    public void activate(Long id) {
        Department department=departmentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Department","Id",id));
        department.setIsActive(true);
        departmentRepository.save(department);

    }

    @Override
    @Transactional(readOnly = true)
    public Department getActiveEntityById(Long id) {
        return departmentRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", id));
    }
}
