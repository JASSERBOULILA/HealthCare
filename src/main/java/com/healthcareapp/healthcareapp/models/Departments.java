package com.healthcareapp.healthcareapp.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "departments")
@Table
@Getter
@Setter
public class Departments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departmentName;




}
