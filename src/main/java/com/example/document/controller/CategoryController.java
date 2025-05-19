package com.example.document.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // ✅ import the new DTO
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.document.dto.CategoryRequest;
import com.example.document.dto.CategorySummary;
import com.example.document.model.Category;
import com.example.document.service.CategoryService;

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
    public List<CategorySummary> getAll() {
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
