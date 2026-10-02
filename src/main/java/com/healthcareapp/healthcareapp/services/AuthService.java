package com.healthcareapp.healthcareapp.services;

import com.healthcareapp.healthcareapp.DTO.SignUpRequest;
import com.healthcareapp.healthcareapp.Repository.UserRepository;
import com.healthcareapp.healthcareapp.exceptions.EmailAlreadyExistsException;
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

    @Transactional
    public User signUp(SignUpRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }
        User user = new User();
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(User.Role.PATIENT);
        return userRepository.save(user);
    }
}
