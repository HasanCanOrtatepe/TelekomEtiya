package com.etiya.etiyatelekom.service.abst;


import com.etiya.etiyatelekom.api.dto.request.complaintRequest.ComplaintCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintListResponse;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintResponse;
import com.etiya.etiyatelekom.entity.Complaint;

public interface ComplaintService {

    ComplaintResponse create(ComplaintCreateRequest request);

    ComplaintResponse getById(Long id);

    ComplaintListResponse getAll();

    ComplaintListResponse getByCustomer(Long customerId);

    void delete(Long id);

    Complaint getEntityById(Long id);
}
