package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.entity.Category;
import com.DipYukti.Ecommerce.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category createCategory(Category category)
    {
        if (categoryRepository.existsByNameIgnoreCase(category.getName()))
        {
            throw new IllegalArgumentException("Category already exists: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, Category updatedCategory)
    {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        existing.setName(updatedCategory.getName());

        return categoryRepository.save(existing);
    }

    @Transactional
    public void deleteCategory(Long id)
    {
        if (!categoryRepository.existsById(id))
        {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long id)
    {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategoryByName(String name)
    {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with name: " + name));
    }
}
