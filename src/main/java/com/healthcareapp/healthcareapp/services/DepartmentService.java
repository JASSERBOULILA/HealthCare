package com.healthcareapp.healthcareapp.services;


import com.healthcareapp.healthcareapp.DTO.Department.DepartmentRequest;
import com.healthcareapp.healthcareapp.DTO.Department.DepartmentResponse;
import com.healthcareapp.healthcareapp.Repository.DepartmentRepository;
import com.healthcareapp.healthcareapp.models.Departments;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;


    //findAll Departments
    public List<Departments> getAllDepartments() {
        return departmentRepository.findAll();
    }


    //create a new Department
    public DepartmentResponse createDepartment(@NotNull Departments departmentRequest) {
        Departments departments = departmentRepository.save(departmentRequest);
        if(departments == null){
            throw new IllegalStateException("Error while Saving");
        }
        return DepartmentResponse.from(departments);
    }


    //Find Department By Id
    public Optional<Departments> findById(Long id) {
        return departmentRepository.findById(id);
    }


}
