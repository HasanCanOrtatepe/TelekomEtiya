package com.etiya.etiyatelekom.service.impl;

import com.etiya.etiyatelekom.api.dto.request.customerRequest.CustomerCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.customerRequest.CustomerUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponse;
import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponseList;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceAlreadyExistsException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.Customer;
import com.etiya.etiyatelekom.repository.CustomerRepository;
import com.etiya.etiyatelekom.service.abst.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapperService modelMapperService;

    @Override
    public CustomerResponse create(CustomerCreateRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Customer", "Email", request.getEmail());
        }
        if (customerRepository.existsByCustomerNo(request.getCustomerNo())) {
            throw new ResourceAlreadyExistsException("Customer", "Customer No", request.getCustomerNo());
        }
        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new ResourceAlreadyExistsException("Customer", "Phone Number", request.getPhone());
        }
        Customer customer = modelMapperService.forRequest().map(request, Customer.class);
        Customer saved = customerRepository.save(customer);
        return modelMapperService.forResponse().map(saved, CustomerResponse.class);
    }

    @Override
    public CustomerResponse update(Long id, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        if (!customer.getEmail().equalsIgnoreCase(request.getEmail()) && customerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Customer", "Email", request.getEmail());
        }
        if (!customer.getPhone().equalsIgnoreCase(request.getPhone()) && customerRepository.existsByPhone(request.getPhone())) {
            throw new ResourceAlreadyExistsException("Customer", "Phone Number", request.getPhone());
        }

        modelMapperService.forRequest().map(request, customer);
        Customer saved= customerRepository.save(customer);

        return modelMapperService.forResponse().map(saved, CustomerResponse.class);
    }

    @Override
    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Customer","id",id));
        return modelMapperService.forResponse().map(customer,CustomerResponse.class);
    }

    @Override
    public CustomerResponseList getAll() {

        List<Customer> customers =customerRepository.findAll();
        List<CustomerResponse> customerResponses= customers.stream().map(c-> modelMapperService.forResponse().map(c,CustomerResponse.class)).toList();
        return CustomerResponseList.builder()
                .customerResponses(customerResponses)
                .count(customerResponses.size())
                .build();
    }

    @Override
    public CustomerResponseList search(String query) {

        return null;
    }

    @Override
    public void delete(Long id) {
        customerRepository.delete(
                customerRepository.findById(id).orElseThrow(
                        ()-> new ResourceNotFoundException("Customer","id",id)
                )
        );
    }
}
