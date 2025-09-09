package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.entity.Category;
import com.DipYukti.Ecommerce.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "categories",key = " 'allCategories' ")
    @Transactional
    public Category createCategory(Category category)
    {
        if (categoryRepository.existsByNameIgnoreCase(category.getName()))
        {
            throw new IllegalArgumentException("Category already exists: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CachePut(value = "categories",key = "#id")
    @CacheEvict(value = "categories",key = "'allCategories'")
    @Transactional
    public Category updateCategory(Long id, Category updatedCategory)
    {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        existing.setName(updatedCategory.getName());

        return categoryRepository.save(existing);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Caching(
            evict = {
                    @CacheEvict(value = "categories",key = "#id"),
                    @CacheEvict(value = "categories",key = " 'allCategories' ")
            }
    )
    @Transactional
    public void deleteCategory(Long id)
    {
        if (!categoryRepository.existsById(id))
        {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Cacheable(value = "categories",key = "#id")
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id)
    {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Cacheable(value = "categories",key = " 'allCategories' ")
    @Transactional(readOnly = true)
    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }

    @Cacheable(value = "categoryByName",key="#name")
    @Transactional(readOnly = true)
    public List<Category> getCategoryByName(String name)
    {
        return categoryRepository.findByNameIgnoringCase(name.trim());
    }
}
