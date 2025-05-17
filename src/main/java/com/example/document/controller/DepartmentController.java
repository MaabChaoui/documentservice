package com.example.document.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.document.dto.DepartmentRequest;
import com.example.document.dto.DepartmentSummary;
import com.example.document.model.Department;
import com.example.document.security.UserContext;
import com.example.document.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public Department createDepartment(@RequestBody DepartmentRequest request) {
        if (!"ADMIN".equals(UserContext.get().getRole())) {
            throw new RuntimeException("Only ADMIN can create departments");
        }
        return departmentService.createDepartment(request);
    }

    // @GetMapping
    // public List<Department> getAllDepartments() {
    //     return departmentService.getAllDepartments();
    // }
    
    @GetMapping
    public List<DepartmentSummary> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department request) {
        return departmentService.updateDepartment(id, request.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }

}
