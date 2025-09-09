package com.DipYukti.Ecommerce.utility;

import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("customSecurity")
@RequiredArgsConstructor
public class CustomSecurity
{
    private final CustomerRepository customerRepository;

    public Boolean hasPermissionAndIsSelf(Long customerId, String permission)
    {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        boolean hasPermission=auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals(permission));
        if(!hasPermission)
        {
            return false;
        }

        String email=auth.getName();
        Customer customer=customerRepository.findById(customerId).orElse(null);
        return customer != null && customer.getEmail().equals(email);
    }

    public boolean hasPermissionAndIsSelfOrAdmin(Long customerId, String permission)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean hasPermission = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(permission));

        if (!hasPermission)
        {
            return false;
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin)
        {
            return true; // Admin can bypass ownership
        }

        String email = auth.getName();

        return customerRepository.findById(customerId)
                .map(customer -> customer.getEmail().equals(email))
                .orElse(false);
    }
}

