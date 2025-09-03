package com.DipYukti.Ecommerce.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponseDto
{
    private Long cartItemId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;


}
