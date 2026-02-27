package com.etiya.etiyatelekom.business.abst;


import com.etiya.etiyatelekom.api.dto.request.complaintRequest.ComplaintCreateRequest;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintListResponse;
import com.etiya.etiyatelekom.api.dto.response.complaintResponse.ComplaintResponse;
import com.etiya.etiyatelekom.entity.Complaint;

public interface ComplaintService {

    ComplaintResponse create(ComplaintCreateRequest request);

    ComplaintResponse getById(Long id);

    ComplaintListResponse getAll();

    ComplaintListResponse getActive();

    ComplaintListResponse getByCustomer(Long customerId);

    Complaint getEntityById(Long id);
}
