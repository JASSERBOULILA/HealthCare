package com.healthcareapp.healthcareapp.controllers;


import com.healthcareapp.healthcareapp.DTO.Department.DepartmentResponse;
import com.healthcareapp.healthcareapp.Repository.DepartmentRepository;
import com.healthcareapp.healthcareapp.models.Departments;
import com.healthcareapp.healthcareapp.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Departments>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

}
