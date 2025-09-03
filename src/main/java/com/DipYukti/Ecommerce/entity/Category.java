package com.DipYukti.Ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category
{
    private Long id;
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
