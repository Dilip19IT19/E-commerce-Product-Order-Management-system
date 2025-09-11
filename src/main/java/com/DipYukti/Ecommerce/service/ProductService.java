package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.dto.AllPageableProductRequestDto;
import com.DipYukti.Ecommerce.dto.AllPageableProductsResponseDto;
import com.DipYukti.Ecommerce.dto.ProductRequestDto;
import com.DipYukti.Ecommerce.dto.ProductResponseDto;
import com.DipYukti.Ecommerce.entity.Product;
import com.DipYukti.Ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "products",key = " 'allProducts' ")
    public Product createProduct(Product product)
    {
        return productRepository.save(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Cacheable(value = "products",key = "#productId")
    @Transactional(readOnly = true)
    public Product getProductById(Long productId)
    {
        return productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException("No product found with this id: "+productId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Cacheable(value = "products",key = " 'allProducts' ")
    @Transactional(readOnly = true)
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    //TODO: Implement getAllPaginatedProducts()

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional(readOnly = true)
    public AllPageableProductsResponseDto getAllPaginatedProducts(AllPageableProductRequestDto requestDto)
    {
        Sort sort=requestDto.getIsAscending()? Sort.by(requestDto.getSortBy()).ascending() : Sort.by(requestDto.getSortBy()).descending();
        Pageable pageable= PageRequest.of(requestDto.getPageNumber(),requestDto.getPageSize(),sort);
        Page<Product> productsPage=productRepository.getAllPaginatedProducts(pageable);
        return AllPageableProductsResponseDto
                .builder()
                .products(productsPage.stream().map(product -> {
                    return ProductResponseDto
                            .builder()
                            .name(product.getName())
                            .description(product.getDescription())
                            .id(product.getId())
                            .categoryName(product.getCategory().getName())
                            .build();
                }).toList())
                .pageNumber(productsPage.getNumber())
                .pageSize(productsPage.getSize())
                .totalElements(productsPage.getTotalElements())
                .totalPages((long) productsPage.getTotalPages())
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Cacheable(value = "productsByCategory",key = "#categoryId")
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryId(Long categoryId)
    {
        return productRepository.findByCategoryId(categoryId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Cacheable(value = "productsByName",key = "#name")
    @Transactional(readOnly = true)
    public List<Product> getAllProductsByName(String name)
    {
        return productRepository.findByNameIgnoringCase(name.trim());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @CachePut(value = "products",key = "#productId")
    @CacheEvict(value = "products",key = " 'allProducts' ")
    public Product updateProduct(Long productId, ProductRequestDto updatedProduct)
    {
        Product oldProduct=productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException("No product found with this id: "+productId));
        oldProduct.setName( updatedProduct.getName()!=null && !updatedProduct.getName().isBlank() ? updatedProduct.getName() : oldProduct.getName() );
        oldProduct.setDescription( updatedProduct.getDescription()!=null && !updatedProduct.getDescription().isBlank() ? updatedProduct.getDescription() : oldProduct.getDescription() );
        oldProduct.setPrice( updatedProduct.getPrice()!=null  ? updatedProduct.getPrice() : oldProduct.getPrice() );
        oldProduct.setStockQuantity( updatedProduct.getStockQuantity()!=null ? updatedProduct.getStockQuantity() : oldProduct.getStockQuantity() );
         productRepository.save(oldProduct);
         return oldProduct;
    }

    //TODO: Implement update product category only

    @PreAuthorize("hasRole('ADMIN')")
    @Caching(
            evict = {
                    @CacheEvict(value = "products",key = "#productId"),
                    @CacheEvict(value = "products",key = " 'allProducts' ")
            }
    )
    @Transactional
    public void deleteProductById(Long productId)
    {
        if(productRepository.existsById(productId))
        {
            productRepository.deleteById(productId);
        }
        else
        {
           throw  new EntityNotFoundException("No product found with this id: "+productId);
        }
    }
}
