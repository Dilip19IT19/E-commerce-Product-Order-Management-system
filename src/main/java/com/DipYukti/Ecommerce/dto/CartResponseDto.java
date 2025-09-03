package com.DipYukti.Ecommerce.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponseDto
{

    private Long customerId;

    private BigDecimal totalPrice;
    private List<CartItemResponseDto> cartItems;

}
