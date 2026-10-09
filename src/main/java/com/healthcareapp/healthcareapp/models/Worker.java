package com.healthcareapp.healthcareapp.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Worker")
@Table
@Getter
@Setter
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id" , nullable = false , unique = true)
    private User user;

    private String employeeNumber;

    private String department;

    private String jobTitle;

    private LocalDate hireDate;

    private String phone;

    private boolean active;




}
