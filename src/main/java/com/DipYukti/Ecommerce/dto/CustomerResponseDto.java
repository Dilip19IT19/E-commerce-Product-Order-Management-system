package com.DipYukti.Ecommerce.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponseDto
{
    private Long id;
    private String name;
    private String email;
    private String address;
}
