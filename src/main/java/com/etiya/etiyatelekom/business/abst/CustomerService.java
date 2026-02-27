package com.etiya.etiyatelekom.business.abst;

import com.etiya.etiyatelekom.api.dto.request.customerRequest.CustomerCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.customerRequest.CustomerUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponse;
import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponseList;
import com.etiya.etiyatelekom.entity.Customer;

public interface CustomerService {

    CustomerResponse create(CustomerCreateRequest request);

    CustomerResponse update(Long id, CustomerUpdateRequest request);

    CustomerResponse getById(Long id);

    CustomerResponseList getAll();

    CustomerResponseList search(String query);

    void delete(Long id);

    Customer getEntityById(Long id);
}