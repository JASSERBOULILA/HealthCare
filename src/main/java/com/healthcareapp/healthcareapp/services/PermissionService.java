
package com.healthcareapp.healthcareapp.services;

import com.healthcareapp.healthcareapp.DTO.permissions.PermissionRequest;
import com.healthcareapp.healthcareapp.DTO.permissions.PermissionResponse;
import com.healthcareapp.healthcareapp.Repository.PermissionRepository;
import com.healthcareapp.healthcareapp.models.Permission;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public List<PermissionResponse> findAll() {
        return permissionRepository
                .findAll(Sort.by("name"))
                .stream()
                .map(PermissionResponse::from)
                .toList();
    }

    @Transactional
    public PermissionResponse create(PermissionRequest request) {

        String name = normalizeName(request.name());

        if (permissionRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Permission already exists"
            );
        }

        Permission permission = new Permission();
        permission.setName(name);

        return save(permission);
    }

    @Transactional
    public PermissionResponse update(Long id, PermissionRequest request) {

        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Permission not found"
                ));

        String name = normalizeName(request.name());

        if (!permission.getName().equalsIgnoreCase(name)
                && permissionRepository.existsByNameIgnoreCase(name)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Permission name already exists"
            );
        }

        permission.setName(name);

        return save(permission);
    }

    private String normalizeName(String name) {
        return name.trim().toUpperCase(Locale.ROOT);
    }

    private PermissionResponse save(Permission permission) {
        try {
            Permission saved = permissionRepository.saveAndFlush(permission);
            return PermissionResponse.from(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Permission violates a database constraint",
                    ex
            );
        }
    }
}
