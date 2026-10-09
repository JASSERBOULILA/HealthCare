
package com.healthcareapp.healthcareapp.controllers;

import com.healthcareapp.healthcareapp.DTO.permissions.PermissionRequest;
import com.healthcareapp.healthcareapp.DTO.permissions.PermissionResponse;
import com.healthcareapp.healthcareapp.services.PermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    private final PermissionService permissionService;

    // GET /permissions
    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    // POST /permissions
    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(
            @Valid @RequestBody PermissionRequest request) {

        PermissionResponse created = permissionService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    // PUT /permissions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponse> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionRequest request) {

        PermissionResponse updated = permissionService.update(id, request);
        return ResponseEntity.ok(updated);
    }
}
