package com.example.document.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.document.dto.UserRequest;
import com.example.document.dto.UserUpdateRequest;
import com.example.document.model.Department;
import com.example.document.model.User;
import com.example.document.repository.DepartmentRepository;
import com.example.document.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    public User createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .role("USER")
                .build();
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // @Transactional
    // public User assignDepartments(Long userId, List<Long> departmentIds) {
    //     User user = userRepository.findById(userId)
    //             .orElseThrow(() -> new RuntimeException("User not found"));
    //     Set<Department> newDepartments = new HashSet<>(departmentRepository.findAllById(departmentIds));
    //     Set<Department> updatedDepartments = new HashSet<>(user.getDepartments());
    //     updatedDepartments.addAll(newDepartments);
    //     user.setDepartments(updatedDepartments);
    //     // Sync with Supabase column
    //     user.setDepartmentIdsString(updatedDepartments.stream()
    //             .map(d -> String.valueOf(d.getId()))
    //             .collect(Collectors.joining(",")));
    //     return userRepository.save(user);
    // }
    @Transactional
    public User assignDepartments(Long userId, List<Long> departmentIds) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1) Load the departments referenced in the payload
        Set<Department> freshSet
                = new HashSet<>(departmentRepository.findAllById(departmentIds));

        // 2) Wipe the user’s existing links and replace with the new ones
        user.getDepartments().clear();        // <-- remove every old link
        user.getDepartments().addAll(freshSet);

        // 3) (legacy) keep the CSV column in sync
        user.setDepartmentIdsString(
                freshSet.stream()
                        .map(d -> String.valueOf(d.getId()))
                        .collect(Collectors.joining(",")));

        return userRepository.save(user);
    }

    @Transactional
    public User unassignDepartments(Long userId, List<Long> departmentIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Department> toRemove = new HashSet<>(departmentRepository.findAllById(departmentIds));

        Set<Department> updatedDepartments = new HashSet<>(user.getDepartments());
        updatedDepartments.removeAll(toRemove);
        user.setDepartments(updatedDepartments);

        // Sync with Supabase column
        user.setDepartmentIdsString(updatedDepartments.stream()
                .map(d -> String.valueOf(d.getId()))
                .collect(Collectors.joining(",")));

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
