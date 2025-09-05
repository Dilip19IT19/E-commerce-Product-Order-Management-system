package com.DipYukti.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto
{

    private Long customerId;

    private BigDecimal totalPrice;
    private List<CartItemResponseDto> cartItems;

}
