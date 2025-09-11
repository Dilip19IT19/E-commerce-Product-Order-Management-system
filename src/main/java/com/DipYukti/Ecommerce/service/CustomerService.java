package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.dto.AllPageableCustomersInputDto;
import com.DipYukti.Ecommerce.dto.AllPageableCustomersOutputDto;
import com.DipYukti.Ecommerce.dto.CustomerResponseDto;
import com.DipYukti.Ecommerce.dto.ResetPasswordRequestDto;
import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService
{
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

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


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public AllPageableCustomersOutputDto getAllPaginatedCustomers(AllPageableCustomersInputDto inputDto)
    {
        Sort sort=inputDto.getIsAscending() ? Sort.by(inputDto.getSortBy()).ascending() : Sort.by(inputDto.getSortBy()).descending();
        Pageable pageable= PageRequest.of(inputDto.getPageNumber(),inputDto.getPageSize(),sort);
        Page<Customer> customersPage=customerRepository.getAllPaginatedCustomers(pageable);
        return AllPageableCustomersOutputDto
                .builder()
                .customers(customersPage.stream().map(customer -> {
                    return CustomerResponseDto
                            .builder()
                            .email(customer.getEmail())
                            .id(customer.getId())
                            .address(customer.getAddress())
                            .name(customer.getName())
                            .build();
                }).toList())
                .pageNumber(customersPage.getNumber())
                .pageSize(customersPage.getSize())
                .totalElements(customersPage.getTotalElements())
                .totalPages(customersPage.getTotalElements())
                .build();
    }

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
    @PreAuthorize("@customSecurity.hasPermissionAndIsSelf(#requestDto.customerId, 'CHANGE_PASSWORD')")
    @Transactional
    public CustomerResponseDto resetPassword(@RequestBody ResetPasswordRequestDto requestDto)
    {
        Customer customer=customerRepository.findById(requestDto.getCustomerId()).orElseThrow();
        customer.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        customer= customerRepository.save(customer);
         return CustomerResponseDto
                 .builder()
                 .address(customer.getAddress())
                 .email(customer.getEmail())
                 .name(customer.getName())
                 .id(customer.getId())
                 .build();
    }


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
