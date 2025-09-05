package com.DipYukti.Ecommerce.controller;

import com.DipYukti.Ecommerce.dto.CategoryRequestDto;
import com.DipYukti.Ecommerce.dto.CategoryResponseDto;
import com.DipYukti.Ecommerce.entity.Category;
import com.DipYukti.Ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController
{
    private final CategoryService categoryService;
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDto request) {
        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category saved = categoryService.createCategory(category);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(categoryService.getCategoryById(id)));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<CategoryResponseDto> getCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(toResponse(categoryService.getCategoryByName(name.trim())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto request) {

        Category updated = Category.builder()
                .name(request.getName())
                .build();

        return ResponseEntity.ok(toResponse(categoryService.updateCategory(id, updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    private CategoryResponseDto toResponse(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
