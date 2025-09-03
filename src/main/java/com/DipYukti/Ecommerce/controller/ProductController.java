package com.DipYukti.Ecommerce.controller;

import com.DipYukti.Ecommerce.dto.ProductRequestDto;
import com.DipYukti.Ecommerce.dto.ProductResponseDto;
import com.DipYukti.Ecommerce.entity.Category;
import com.DipYukti.Ecommerce.entity.Product;
import com.DipYukti.Ecommerce.repository.CategoryRepository;
import com.DipYukti.Ecommerce.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController
{
    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequestDto requestDto)
    {
        try
        {
            Category category=null;
            if(requestDto.getCategoryId()!=null)
            {
                category=categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(()->new EntityNotFoundException("No category found with id: "+requestDto.getCategoryId()));
            }
            Product product=Product
                    .builder()
                    .name(requestDto.getName())
                    .price(requestDto.getPrice())
                    .stockQuantity(requestDto.getStockQuantity())
                    .build();
            if(category!=null)
            {
                product.setCategory(category);
            }
            Product savedProduct= productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ProductResponseDto.builder()
                            .id(savedProduct.getId())
                            .description(savedProduct.getDescription())
                            .price(savedProduct.getPrice())
                            .stockQuantity(savedProduct.getStockQuantity())
                            .categoryName(savedProduct.getCategory().getName())
            );
        }
        catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchAllProducts(@PathVariable Long id)
    {
        try
        {
            List<Product>products= productService.getAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(products);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto requestDto)
    {
        try
        {
            Category category=null;
            if(requestDto.getCategoryId()!=null)
            {
                category=categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(()->new EntityNotFoundException("No category found with id: "+requestDto.getCategoryId()));
            }
            Product product=Product
                    .builder()
                    .name(requestDto.getName())
                    .price(requestDto.getPrice())
                    .stockQuantity(requestDto.getStockQuantity())
                    .build();
            if(category!=null)
            {
                product.setCategory(category);
            }
            Product updatedProduct= productService.updateProduct(id,product);
            return ResponseEntity.status(HttpStatus.OK).body(
                    ProductResponseDto.builder()
                            .id(updatedProduct.getId())
                            .description(updatedProduct.getDescription())
                            .price(updatedProduct.getPrice())
                            .stockQuantity(updatedProduct.getStockQuantity())
                            .categoryName(updatedProduct.getCategory().getName())
            );
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id)
    {
        try
        {
            productService.deleteProductById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product deleted successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestParam String search)
    {
        try
        {

            List<Product>productList = productService.getAllProductsByName(search);
            List<ProductResponseDto> productResponseDtoList=productList.stream().map((product -> {
                return ProductResponseDto
                        .builder()
                        .id(product.getId())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .stockQuantity(product.getStockQuantity())
                        .categoryName(product.getCategory()!=null ?  product.getCategory().getName() : null)
                        .build();

            })).toList();
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDtoList);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
