package com.DipYukti.Ecommerce.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponseDto
{
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}
