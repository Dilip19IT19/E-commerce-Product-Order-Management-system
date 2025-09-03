package com.DipYukti.Ecommerce.repository;

import com.DipYukti.Ecommerce.entity.CartItem;
import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItem, Long>
{
    public List<CartItem> findByCustomer(Customer customer);
    public void deleteByCustomer(Customer customer);
    public Optional<CartItem> findByProductAndCustomer(Product product, Customer customer);
}
