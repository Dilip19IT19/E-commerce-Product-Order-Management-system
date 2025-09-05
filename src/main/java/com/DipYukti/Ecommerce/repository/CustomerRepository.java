package com.DipYukti.Ecommerce.repository;

import com.DipYukti.Ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>
{
    Optional<Customer> findByEmail(String email);
    @Query(value = " SELECT * FROM customers WHERE name ILIKE %:name% ",nativeQuery = true)
    List<Customer> findByNameIgnoringCase(@Param(value = "name")String name);
}
