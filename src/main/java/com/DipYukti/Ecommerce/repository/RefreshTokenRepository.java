package com.DipYukti.Ecommerce.repository;

import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long>
{
    Optional<RefreshToken> findByToken(String token);
    void deleteByCustomer(Customer customer);
    Optional<RefreshToken> findByCustomer(Customer customer);
}
