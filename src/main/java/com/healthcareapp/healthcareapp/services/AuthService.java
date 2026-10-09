package com.healthcareapp.healthcareapp.services;

import com.healthcareapp.healthcareapp.DTO.SignUpRequest;
import com.healthcareapp.healthcareapp.Repository.RoleRepository;
import com.healthcareapp.healthcareapp.Repository.UserRepository;
import com.healthcareapp.healthcareapp.exceptions.EmailAlreadyExistsException;
import com.healthcareapp.healthcareapp.models.Roles;
import com.healthcareapp.healthcareapp.models.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public User signUp(SignUpRequest req) {

        Roles patientRole = roleRepository.findByName(req.role())
                .orElseGet(() -> {
                    Roles role = new Roles();
                    role.setName("PATIENT");
                    return roleRepository.save(role);
                });

        User user = new User();

        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        // Every normal signup is a PATIENT
        user.setRole(patientRole);

        return userRepository.save(user);
    }
}
