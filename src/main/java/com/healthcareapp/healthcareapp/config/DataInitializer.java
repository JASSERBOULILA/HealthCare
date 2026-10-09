package com.healthcareapp.healthcareapp.config;

import com.healthcareapp.healthcareapp.Repository.PermissionRepository;
import com.healthcareapp.healthcareapp.Repository.RoleRepository;
import com.healthcareapp.healthcareapp.Repository.UserRepository;
import com.healthcareapp.healthcareapp.models.Permission;
import com.healthcareapp.healthcareapp.models.Roles;
import com.healthcareapp.healthcareapp.models.User;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeDatabase(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            // ------------------------
            // PATIENT PERMISSIONS
            // ------------------------
            Permission viewOwnProfile =
                    createPermission(
                            permissionRepository,
                            "VIEW_OWN_PROFILE"
                    );
            Permission updateOwnProfile  =
                    createPermission(
                            permissionRepository,
                            "UPDATE_OWN_PROFILE"
                    );

            Permission viewOwnMedicalRecords   =
                    createPermission(
                            permissionRepository,
                            "VIEW_OWN_MEDICAL_RECORDS"
                    );



            // ------------------------
            // DOCTOR PERMISSIONS
            // ------------------------

            Permission readPatients =
                    createPermission(
                            permissionRepository,
                            "READ_PATIENTS"
                    );

            Permission readMedicalRecords =
                    createPermission(
                            permissionRepository,
                            "READ_MEDICAL_RECORDS"
                    );

            Permission writeMedicalRecords =
                    createPermission(
                            permissionRepository,
                            "WRITE_MEDICAL_RECORDS"
                    );

            Permission createPrescription =
                    createPermission(
                            permissionRepository,
                            "CREATE_PRESCRIPTION"
                    );

            Permission createDocument =
                    createPermission(
                            permissionRepository,
                            "CREATE_DOCUMENT"
                    );

            Permission viewAppointments =
                    createPermission(
                            permissionRepository,
                            "VIEW_APPOINTMENTS"
                    );


            // ------------------------
            // NURSE PERMISSIONS
            // ------------------------

            Permission updateVitals =
                    createPermission(
                            permissionRepository,
                            "UPDATE_VITALS"
                    );


            // ------------------------
            // ADMIN PERMISSIONS
            // ------------------------

            Permission manageUsers =
                    createPermission(
                            permissionRepository,
                            "MANAGE_USERS"
                    );

            Permission assignRoles =
                    createPermission(
                            permissionRepository,
                            "ASSIGN_ROLES"
                    );

            Permission manageRooms =
                    createPermission(
                            permissionRepository,
                            "MANAGE_ROOMS"
                    );

            Permission manageDepartments =
                    createPermission(
                            permissionRepository,
                            "MANAGE_DEPARTMENTS"
                    );


            // ========================
            // PATIENT ROLE
            // ========================

            Roles patientRole =
                    createRole(
                            roleRepository,
                            "PATIENT",
                            Set.of(
                                    viewOwnProfile,
                                    viewOwnMedicalRecords,
                                    updateOwnProfile
                            )
                    );


            // ========================
            // DOCTOR ROLE
            // ========================

            Roles doctorRole =
                    createRole(
                            roleRepository,
                            "DOCTOR",
                            Set.of(
                                    readPatients,
                                    readMedicalRecords,
                                    writeMedicalRecords,
                                    createPrescription,
                                    createDocument,
                                    viewAppointments
                            )
                    );


            // ========================
            // NURSE ROLE
            // ========================

            Roles nurseRole =
                    createRole(
                            roleRepository,
                            "NURSE",
                            Set.of(
                                    readPatients,
                                    readMedicalRecords,
                                    updateVitals,
                                    viewAppointments
                            )
                    );


            // ========================
            // ADMIN ROLE
            // ========================

            Roles adminRole =
                    createRole(
                            roleRepository,
                            "ADMIN",
                            Set.of(
                                    manageUsers,
                                    assignRoles,
                                    manageRooms,
                                    manageDepartments
                            )
                    );


            // ========================
            // DEFAULT ADMIN
            // ========================

            String adminEmail = "bhimlol123456@gmail.com";

            if (!userRepository.existsByEmail(adminEmail)) {

                User admin = new User();

                admin.setFirstName("System");
                admin.setLastName("Admin");
                admin.setEmail(adminEmail);

                admin.setPassword(
                        passwordEncoder.encode("ChangeMe123!")
                );

                admin.setRole(adminRole);

                userRepository.save(admin);
            }
        };
    }


    private Permission createPermission(
            PermissionRepository repository,
            String name
    ) {

        return repository.findByName(name)
                .orElseGet(() -> {
                    Permission permission =
                            new Permission();

                    permission.setName(name);

                    return repository.save(permission);
                });
    }


    private Roles createRole(
            RoleRepository repository,
            String name,
            Set<Permission> permissions
    ) {

        Roles role =
                repository.findByName(name)
                        .orElseGet(() -> {

                            Roles newRole =
                                    new Roles();

                            newRole.setName(name);

                            return repository.save(newRole);
                        });

        role.setPermissions(
                new HashSet<>(permissions)
        );

        return repository.save(role);
    }
}