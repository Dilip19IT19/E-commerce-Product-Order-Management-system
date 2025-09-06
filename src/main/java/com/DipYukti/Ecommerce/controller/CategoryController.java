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
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDto request)
    {
        try
        {
            Category category = Category.builder()
                    .name(request.getName())
                    .build();

            Category saved = categoryService.createCategory(category);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<?> getAllCategories()
    {
        try
        {
            List<CategoryResponseDto> categories = categoryService.getAllCategories()
                    .stream()
                    .map(this::toResponse)
                    .toList();
            return ResponseEntity.ok(categories);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id)
    {
        try
        {
            return ResponseEntity.ok(toResponse(categoryService.getCategoryById(id)));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String name)
    {
        try
        {

            List<CategoryResponseDto>categoryResponseDtoList=categoryService
                    .getCategoryByName(name.trim()).stream()
                    .map((this::toResponse)).toList();
            return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDtoList);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto request)
    {
        try
        {
            Category updated = Category.builder()
                    .name(request.getName())
                    .build();

            return ResponseEntity.ok(toResponse(categoryService.updateCategory(id, updated)));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id)
    {
        try
        {
            categoryService.deleteCategory(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("category deleted successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }
    private CategoryResponseDto toResponse(Category category)
    {
         return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();


    }

}
