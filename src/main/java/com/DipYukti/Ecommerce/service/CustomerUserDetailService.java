package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.entity.CustomerUserDetails;
import com.DipYukti.Ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerUserDetailService implements UserDetailsService
{
    private final CustomerRepository customerRepository;

    @Override
    public CustomerUserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Customer customer=customerRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("No user found with id: "+username));
        return new CustomerUserDetails(customer);

    }
}
