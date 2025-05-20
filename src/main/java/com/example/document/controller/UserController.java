package com.example.document.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.document.dto.UserRequest;
import com.example.document.dto.UserResponse;
import com.example.document.dto.UserUpdateRequest;
import com.example.document.model.User;
import com.example.document.service.UserQueryService;
import com.example.document.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserQueryService queryService;

    public UserController(UserService userService,
            UserQueryService queryService) {
        this.userService = userService;
        this.queryService = queryService;
    }

    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        User user = userService.createUser(request);
        return new UserResponse(user);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        User user = userService.updateUser(id, request);
        return new UserResponse(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * GET /api/users?page=1&perPage=5&search=foo
     */
    @GetMapping
    public ResponseEntity<Page<UserResponse>> allUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int perPage,
            @RequestParam(defaultValue = "") String search) {

        Page<UserResponse> result = queryService.list(page, perPage, search);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PutMapping("/{id}/assign-departments")
    public UserResponse assignDepartments(@PathVariable Long id, @RequestBody List<Long> departmentIds) {
        User user = userService.assignDepartments(id, departmentIds);
        return new UserResponse(user);
    }

    @PutMapping("/{id}/unassign-departments")
    public UserResponse unassignDepartments(@PathVariable Long id, @RequestBody List<Long> departmentIds) {
        User user = userService.unassignDepartments(id, departmentIds);
        return new UserResponse(user);
    }
}
