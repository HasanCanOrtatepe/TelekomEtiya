package com.etiya.etiyatelekom.api.controller;

import com.etiya.etiyatelekom.api.dto.request.customerRequest.CustomerUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponse;
import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponseList;
import com.etiya.etiyatelekom.business.abst.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
@PreAuthorize("hasRole('ADMIN')")
public class CustomerController {

    private final CustomerService customerService;

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody CustomerUpdateRequest request) {
        return ResponseEntity.ok(customerService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @GetMapping
    public ResponseEntity<CustomerResponseList> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
