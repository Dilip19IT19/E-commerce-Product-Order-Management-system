package com.DipYukti.Ecommerce.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderResponseDto
{
    private Long orderId;
    private Long customerId;
    private BigDecimal totalAmount;
    private String orderStatus;
    private List<OrderItemResponseDto> orderItems;
}
