package com.DipYukti.Ecommerce.repository;

import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>
{
    public List<Order> findByCustomer(Customer customer);
}
