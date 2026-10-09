package com.healthcareapp.healthcareapp.controllers;

import com.healthcareapp.healthcareapp.DTO.Worker.WorkerCreateRequest;
import com.healthcareapp.healthcareapp.DTO.Worker.WorkerResponse;
import com.healthcareapp.healthcareapp.models.Worker;
import com.healthcareapp.healthcareapp.services.profiles.WorkerService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;


    @PostMapping("/create/{departmentId}")
    public ResponseEntity<WorkerResponse> addWorker(@Valid @RequestBody WorkerCreateRequest request,
                                                    @PathVariable Long departmentId) {

    Worker worker = workerService.createWorker(request, departmentId);

    return ResponseEntity.status(HttpStatus.CREATED).body(WorkerResponse.from(worker));

    }

    @GetMapping("/allWorkers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Worker>> getAllWorkers() {

        return ResponseEntity.ok(
                workerService.getAllWorkers()
        );
    }

}
