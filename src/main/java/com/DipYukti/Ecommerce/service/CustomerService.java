package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.repository.CustomerRepository;
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
public class CustomerService
{
    private final CustomerRepository customerRepository;

    @CacheEvict(value = "customers",key="'allCustomers'")
    public Customer createCustomer(Customer customer)
    {
        return customerRepository.save(customer);
    }


    @PreAuthorize("@customSecurity.hasPermissionAndIsSelfOrAdmin(#customerId,'READ_CUSTOMER')")
    @Cacheable(value = "customers",key = "#customerId")
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long customerId)
    {
        return customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("No Customer found with this id: "+customerId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Cacheable(value = "customers", key = " 'allCustomers' ")
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers()
    {
        return customerRepository.findAll();
    }

    //TODO: Implement getAllPaginatedCustomers()

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    @Cacheable(value = "customersByName",key = "#name")
    public List<Customer> getAllCustomersByName(String name)
    {
        return customerRepository.findByNameIgnoringCase(name);
    }

    @PreAuthorize("@customSecurity.hasPermissionAndIsSelf(#customerId, 'UPDATE_CUSTOMER')")
    @Transactional
    @CachePut(value = "customers",key = "#customerId")
    @CacheEvict(value = "customers",key = " 'allCustomers' ")
    public Customer updateCustomer(Long customerId, Customer updatedCustomer)
    {
        Customer oldCustomer=customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("No Customer found with this id: "+customerId));
        oldCustomer.setName( updatedCustomer.getName()!=null && !updatedCustomer.getName().isBlank() ? updatedCustomer.getName() : oldCustomer.getName() );
        oldCustomer.setAddress( updatedCustomer.getAddress()!=null && !updatedCustomer.getAddress().isBlank() ? updatedCustomer.getAddress() : oldCustomer.getAddress() );
       return customerRepository.save(oldCustomer);
    }

    // TODO: Implement resetPassword()

    // TODO: Implement updateEmail()

    @PreAuthorize("@customSecurity.hasPermissionAndIsSelfOrAdmin(#customerId,'DELETE_CUSTOMER')")
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "customers", key = "#customerId"),
                    @CacheEvict(value = "customers", key = " 'allCustomers' ")
            }
    )
    public void deleteCustomerById(Long customerId)
    {
        if(customerRepository.existsById(customerId))
        {
            customerRepository.deleteById(customerId);
        }
        else
        {
            throw  new EntityNotFoundException("No customer found with this id: "+customerId);
        }
    }



}
