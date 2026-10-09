package com.healthcareapp.healthcareapp.controllers;


import com.healthcareapp.healthcareapp.DTO.UserResponse;
import com.healthcareapp.healthcareapp.DTO.Worker.WorkerCreateRequest;
import com.healthcareapp.healthcareapp.DTO.Worker.WorkerResponse;
import com.healthcareapp.healthcareapp.DTO.Worker.WorkerUpdateRequest;
import com.healthcareapp.healthcareapp.models.Departments;
import com.healthcareapp.healthcareapp.models.User;
import com.healthcareapp.healthcareapp.models.Worker;
import com.healthcareapp.healthcareapp.services.UserService;
import com.healthcareapp.healthcareapp.services.profiles.WorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final WorkerService workerService;
    private final UserService userService;

    // GET /admin/users
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userService.findAllUsers()
                .stream()
                .map(UserResponse::from)
                .toList();

        return ResponseEntity.ok(users);
    }

    // GET /admin/workers
    @GetMapping("/workers")
    public ResponseEntity<List<WorkerResponse>> getAllWorkers() {

        List<WorkerResponse> workers = workerService.getAllWorkers()
                .stream()
                .map(WorkerResponse::from)
                .toList();

        return ResponseEntity.ok(workers);
    }

    // POST /admin/workers
    @PostMapping("/workers/{departmentId}")
    public ResponseEntity<WorkerResponse> createWorker(
            @Valid @RequestBody WorkerCreateRequest request , @PathVariable Long departmentId) {

        Worker worker = workerService.createWorker(request , departmentId);

        if (worker == null) {
            throw new IllegalStateException(
                    "Worker service returned null after creation"
            );
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(WorkerResponse.from(worker));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteWorker(@PathVariable Long id) throws  Exception{
        Worker oneWorker = workerService.findUserById(id);
        if(oneWorker == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        workerService.deleteWorker(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("the worker " + oneWorker.getUser().getFirstName() + oneWorker.getUser().getLastName() + " has been deleted");
    }

    @PatchMapping("/update/worker/{id}/{departmentId}")
    public ResponseEntity<WorkerResponse> updateWorker(
            @Valid @RequestBody WorkerUpdateRequest request,
            @PathVariable Long id,
            @PathVariable Departments departmentId
    ) {
        Worker worker = workerService.updateWorker(id, request , departmentId);

        return ResponseEntity.ok(WorkerResponse.from(worker));
    }

}
