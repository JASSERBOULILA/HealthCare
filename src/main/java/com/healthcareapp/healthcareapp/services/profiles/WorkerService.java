package com.healthcareapp.healthcareapp.services.profiles;


import com.healthcareapp.healthcareapp.DTO.Worker.WorkerCreateRequest;
import com.healthcareapp.healthcareapp.DTO.Worker.WorkerResponse;
import com.healthcareapp.healthcareapp.DTO.Worker.WorkerUpdateRequest;
import com.healthcareapp.healthcareapp.Repository.UserRepository;
import com.healthcareapp.healthcareapp.Repository.WorkerRepository;
import com.healthcareapp.healthcareapp.exceptions.ResourceNotFoundException;
import com.healthcareapp.healthcareapp.models.User;
import com.healthcareapp.healthcareapp.models.Worker;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    public WorkerService(WorkerRepository workerRepository, UserRepository userRepository) {
        this.workerRepository = workerRepository;
        this.userRepository = userRepository;
    };


    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker findUserById(Long id){
        return workerRepository.findById(id).orElseThrow((
                )-> new ResourceNotFoundException("Worker with id: " + id + " not found"));
    };

    public Worker createWorker(@NonNull WorkerCreateRequest request) {

        User user = userRepository.findById(request.user_id())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Worker worker = new Worker();
        worker.setUser(user);
        worker.setEmployeeNumber(request.employeeNumber());
        worker.setDepartment(request.department());
        worker.setJobTitle(request.jobTitle());
        worker.setHireDate(request.hireDate());
        worker.setPhone(request.phone());
        worker.setActive(true);

        return workerRepository.save(worker);
    }

    @Transactional
    public Worker updateWorker(Long id, WorkerUpdateRequest request) {

        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Worker with id: " + id + " not found"
                ));

        if (request.employeeNumber() != null) {
            worker.setEmployeeNumber(request.employeeNumber());
        }

        if (request.department() != null) {
            worker.setDepartment(request.department());
        }

        if (request.jobTitle() != null) {
            worker.setJobTitle(request.jobTitle());
        }

        if (request.hireDate() != null) {
            worker.setHireDate(request.hireDate());
        }

        if (request.phone() != null) {
            worker.setPhone(request.phone());
        }

        if (request.active() != null) {
            worker.setActive(request.active());
        }

        return workerRepository.save(worker);
    }


    @Transactional
    public void deleteWorker(Long id) {

        Worker worker = workerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Worker with id: " + id + " not found"
                        )
                );

        workerRepository.delete(worker);
    }




}
