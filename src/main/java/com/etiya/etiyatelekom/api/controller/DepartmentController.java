package com.etiya.etiyatelekom.api.controller;

import com.etiya.etiyatelekom.api.dto.request.departmentRequest.DepartmentCreateRequest;
import com.etiya.etiyatelekom.api.dto.request.departmentRequest.DepartmentUpdateRequest;
import com.etiya.etiyatelekom.api.dto.response.departmentResponse.DepartmentListResponse;
import com.etiya.etiyatelekom.api.dto.response.departmentResponse.DepartmentResponse;
import com.etiya.etiyatelekom.service.abst.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable Long id, @Valid @RequestBody DepartmentUpdateRequest request) {
        return ResponseEntity.ok(departmentService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<DepartmentListResponse> getAll() {
        return ResponseEntity.ok(departmentService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<DepartmentListResponse> getActive() {
        return ResponseEntity.ok(departmentService.getActive());
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        departmentService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        departmentService.activate(id);
        return ResponseEntity.noContent().build();
    }
}
