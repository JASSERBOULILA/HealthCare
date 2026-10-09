package com.healthcareapp.healthcareapp.DTO.Department;

import com.healthcareapp.healthcareapp.models.Departments;

public record DepartmentResponse(
        Long id,
        String departmentName
){
    public static DepartmentResponse from(
            Departments departments
    ){
        return new DepartmentResponse(
                departments.getId(),
                departments.getDepartmentName()
        );
    }
}
