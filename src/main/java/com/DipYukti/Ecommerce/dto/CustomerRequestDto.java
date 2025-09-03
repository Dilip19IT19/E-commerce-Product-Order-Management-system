package com.DipYukti.Ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequestDto
{
    @NotBlank(message = "customer name is required")
    private String name;
    @Size(min = 3, max = 15, message = "password length must be between 3 and 15")
    private String password;
    @Email(message = "valid email must be provided")
    private String email;
    private String address;
}
