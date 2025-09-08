package com.DipYukti.Ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto
{

    @Email(message = "valid email is required")
    private String username;
    @Size(min=3, max=20, message = "password length must be between 3 and 20")
    private String password;

}
