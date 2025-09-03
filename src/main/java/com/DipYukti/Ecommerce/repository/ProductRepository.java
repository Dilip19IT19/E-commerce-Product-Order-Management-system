package com.DipYukti.Ecommerce.repository;

import com.DipYukti.Ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>
{
    @Query(value = " SELECT * FROM products WHERE name ILIKE '%name%' ",nativeQuery = true)
    List<Product> findByNameIgnoringCase(@Param(value = "name")String name);
    List<Product> findByCategoryId(Long categoryId);

}
