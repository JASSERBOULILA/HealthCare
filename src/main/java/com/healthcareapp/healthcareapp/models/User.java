package com.healthcareapp.healthcareapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    public enum Role {
        PATIENT, DOCTOR, ADMIN
    }

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore // never serialize the hash, even by accident
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.PATIENT;

}
