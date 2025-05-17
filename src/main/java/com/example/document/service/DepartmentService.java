package com.example.document.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.document.dto.DepartmentRequest;
import com.example.document.dto.DepartmentSummary;
import com.example.document.model.Department;
import com.example.document.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(DepartmentRequest request) {
        Department department = new Department();
        department.setName(request.getName());
        return departmentRepository.save(department);
    }

    // public List<Department> getAllDepartments() {
    //     return departmentRepository.findAll();
    // }
    public List<DepartmentSummary> getAllDepartments() {
        return departmentRepository.findAllWithUserCount();
    }

    public Department updateDepartment(Long id, String newName) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        department.setName(newName);
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        departmentRepository.delete(department);
    }

}
