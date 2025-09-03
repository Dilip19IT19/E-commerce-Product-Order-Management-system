package com.DipYukti.Ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequestDto
{
    @NotBlank(message = "customer id is required")
    private Long customerId;
}
