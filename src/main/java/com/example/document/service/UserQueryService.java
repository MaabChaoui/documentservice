package com.example.document.service;

import com.example.document.dto.UserResponse;
import com.example.document.model.User;
import com.example.document.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {

    private final UserRepository userRepo;

    @Autowired
    public UserQueryService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Page<UserResponse> list(
            int page, int perPage, String search) {

        // Spring’s PageRequest is *zero-based*, your UI is 1-based
        Pageable pageable = PageRequest.of(
                Math.max(page - 1, 0),
                perPage,
                Sort.by("createdAt").descending());

        Page<User> slice;
        if (search != null && !search.isBlank()) {
            slice = userRepo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    search, search, pageable);
        } else {
            slice = userRepo.findAll(pageable);
        }

        return slice.map(UserResponse::new);
    }
}
