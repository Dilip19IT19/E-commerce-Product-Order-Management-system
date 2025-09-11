package com.DipYukti.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllPageableProductsResponseDto
{
    private List<ProductResponseDto> products;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalPages;
    private Long totalElements;
}
