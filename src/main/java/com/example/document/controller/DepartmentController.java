package com.example.document.controller;

import com.example.document.dto.DepartmentRequest;
import com.example.document.model.Department;
import com.example.document.service.DepartmentService;
import com.example.document.security.UserContext;
import org.springframework.web.bind.annotation.*;
import java.util.List;


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
    @GetMapping
    public List<Department> getAllDepartments() {
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
