package com.etiya.etiyatelekom.service.abst;


import com.etiya.etiyatelekom.api.dto.request.departmentRequest.DepartmentCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.departmentRequest.DepartmentUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.departmentResponse.DepartmentListResponse;
import com.etiya.etiyatelekom.api.dto.response.departmentResponse.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse create(DepartmentCreateRequest request);

    DepartmentResponse update(Long id, DepartmentUpdateRequest request);

    DepartmentResponse getById(Long id);

    DepartmentListResponse getAll();

    DepartmentListResponse getActive();

    void deactivate(Long id);
}
