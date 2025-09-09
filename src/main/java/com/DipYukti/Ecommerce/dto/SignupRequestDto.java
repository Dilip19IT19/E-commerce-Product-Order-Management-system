package com.DipYukti.Ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto
{
    @NotBlank(message = "Name is required")
    private String name;
    @Email(message = "Valid email must be provide")
    private String email;
    @Size(min = 3, max = 20, message = "password length must be between 3 and 20")
    private String password;
    private String address;
}
