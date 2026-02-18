package com.etiya.etiyatelekom.api.controller;


import com.etiya.etiyatelekom.api.dto.request.customerRequest.CustomerCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.customerRequest.CustomerUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponse;
import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponseList;
import com.etiya.etiyatelekom.service.abst.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("customer/deneme")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerCreateRequest customerCreateRequest){
        return new ResponseEntity<>(customerService.create(customerCreateRequest) , HttpStatus.CREATED);

    }
    @PutMapping("customer/deneme/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest ,@PathVariable Long id){
        return new ResponseEntity<>(customerService.update(id,customerUpdateRequest) , HttpStatus.OK);

    }
    @GetMapping("customer/deneme/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id){
        return new ResponseEntity<>(customerService.getById(id) , HttpStatus.OK);
    }
    @GetMapping("customer/deneme")
    public ResponseEntity<CustomerResponseList> getAllCustomer(){
        return new ResponseEntity<>(customerService.getAll(),HttpStatus.OK);
    }
    @DeleteMapping("customer/deneme/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Long id){
        customerService.delete(id);
        return  ResponseEntity.noContent().build();
    }





}
