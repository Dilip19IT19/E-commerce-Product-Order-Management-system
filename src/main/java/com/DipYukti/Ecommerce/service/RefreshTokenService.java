package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.entity.RefreshToken;
import com.DipYukti.Ecommerce.repository.CustomerRepository;
import com.DipYukti.Ecommerce.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService
{
    private final CustomerRepository customerRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Transactional
    public RefreshToken createRefreshToken(Long customerId)
    {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        RefreshToken oldRefreshToken=refreshTokenRepository.findByCustomer(customer).orElse(null);
        if(oldRefreshToken!=null)
        {
            refreshTokenRepository.delete(oldRefreshToken);
            refreshTokenRepository.flush();
        }
        RefreshToken refreshToken= RefreshToken
                .builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .customer(customer)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public boolean isRefreshTokenExpired(RefreshToken refreshToken)
    {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }

    @Transactional
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(()->new IllegalArgumentException("Refresh token is expired: "+token));
    }

    @Transactional
    public void deleteByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("Customer not found with id: "+customerId));
         refreshTokenRepository.deleteByCustomer(customer);
    }
    @Transactional
    public void deleteByToken(RefreshToken oldRefreshToken) {
        refreshTokenRepository.deleteById(oldRefreshToken.getId());
    }

    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken oldRefreshToken)
    {
        RefreshToken newRefreshToken=RefreshToken
                .builder()
                .token(UUID.randomUUID().toString())
                .customer(oldRefreshToken.getCustomer())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();
        refreshTokenRepository.deleteById(oldRefreshToken.getId());
        return refreshTokenRepository.save(newRefreshToken);
    }

    @Transactional
    public void revokeRefreshTokenByCustomerId(Long customerId)
    {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer!=null)
        {
            refreshTokenRepository.deleteByCustomer(customer);
        }
    }



}
