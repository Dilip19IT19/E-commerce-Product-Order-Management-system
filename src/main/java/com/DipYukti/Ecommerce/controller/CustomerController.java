package com.DipYukti.Ecommerce.controller;

import com.DipYukti.Ecommerce.dto.CustomerRequestDto;
import com.DipYukti.Ecommerce.dto.CustomerResponseDto;
import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController
{
    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable   Long customerId)
    {
        try
        {
            Customer customer=customerService.getCustomerById(customerId);
            CustomerResponseDto responseDto=CustomerResponseDto
                    .builder()
                    .email(customer.getEmail())
                    .id(customer.getId())
                    .name(customer.getName())
                    .address(customer.getAddress())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllCustomers()
    {
        try
        {

            List<Customer> customers=customerService.getAllCustomers();
            List<CustomerResponseDto> responseDtoList=customers.stream().map(customer -> {
                return CustomerResponseDto
                        .builder()
                        .email(customer.getEmail())
                        .id(customer.getId())
                        .name(customer.getName())
                        .address(customer.getAddress())
                        .build();
            }).toList();
            return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long customerId, @RequestBody CustomerRequestDto requestDto)
    {
        try
        {
            Customer customer= Customer
                    .builder()
                    .name(requestDto.getName())
                    .email(requestDto.getEmail())
                    .address(requestDto.getAddress())
                    .build();
            Customer updatedCustomer=customerService.updateCustomer(customerId, customer);
            CustomerResponseDto responseDto=CustomerResponseDto
                    .builder()
                    .email(updatedCustomer.getEmail())
                    .id(updatedCustomer.getId())
                    .name(updatedCustomer.getName())
                    .address(updatedCustomer.getAddress())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);

        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId)
    {
        try
        {
            customerService.deleteCustomerById(customerId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Customer deleted successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomerByName(@RequestParam String name)
    {
        try
        {
            List<Customer> customers=customerService.getAllCustomersByName(name);
            List<CustomerResponseDto> responseDtoList=customers.stream().map(customer -> {
                return CustomerResponseDto
                        .builder()
                        .email(customer.getEmail())
                        .id(customer.getId())
                        .name(customer.getName())
                        .address(customer.getAddress())
                        .build();
            }).toList();
            return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
