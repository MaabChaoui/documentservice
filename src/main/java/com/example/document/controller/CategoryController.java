package com.example.document.controller;

import com.example.document.model.Category;
import com.example.document.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import com.example.document.dto.CategoryRequest; // ✅ import the new DTO


import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ✅ Create category using JSON
    @PostMapping
    public Category create(@RequestBody CategoryRequest request) {
        return categoryService.createCategory(request.getName());
    }

    // ✅ Get all categories
    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAllCategories();
    }

    // ✅ Update category using JSON
    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestBody CategoryRequest request) {
        return categoryService.updateCategory(id, request.getName());
    }

    // ✅ Delete category
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
