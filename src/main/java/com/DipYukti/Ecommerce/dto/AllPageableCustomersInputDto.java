package com.DipYukti.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllPageableCustomersInputDto
{
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean isAscending;
    private String  sortBy;
}
