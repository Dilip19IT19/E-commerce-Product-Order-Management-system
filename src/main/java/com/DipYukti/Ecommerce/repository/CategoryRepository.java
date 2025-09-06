package com.DipYukti.Ecommerce.repository;

import com.DipYukti.Ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>
{
    @Query(value = " SELECT * FROM categories WHERE name ILIKE %:name% ",nativeQuery = true)
    List<Category> findByNameIgnoringCase(@Param(value = "name")String name);
    boolean existsByNameIgnoreCase(String name);
}
