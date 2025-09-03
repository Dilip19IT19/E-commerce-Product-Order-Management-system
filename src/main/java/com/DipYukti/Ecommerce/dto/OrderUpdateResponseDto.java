package com.DipYukti.Ecommerce.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderUpdateResponseDto
{
    private Long orderId;
    private String orderStatus;
}
