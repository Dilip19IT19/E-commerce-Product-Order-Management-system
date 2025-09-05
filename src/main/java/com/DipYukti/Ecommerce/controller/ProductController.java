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

    @PostMapping("/create")
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
                    .description(requestDto.getDescription())
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
                            .name(product.getName())
                            .description(savedProduct.getDescription())
                            .price(savedProduct.getPrice())
                            .stockQuantity(savedProduct.getStockQuantity())
                            .categoryName(savedProduct.getCategory().getName())
                            .build()
            );
        }
        catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchProductById(@PathVariable Long id)
    {
        try
        {
            Product product= productService.getProductById(id);
            ProductResponseDto responseDto= ProductResponseDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stockQuantity(product.getStockQuantity())
                    .categoryName(product.getCategory().getName())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<?> fetchAllProducts()
    {
        try
        {
            List<Product>products= productService.getAllProducts();
            List<ProductResponseDto>productResponseDtoList=products.stream().map((product -> {
                return ProductResponseDto.builder()
                        .id(product.getId())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .name(product.getName())
                        .stockQuantity(product.getStockQuantity())
                        .categoryName(product.getCategory().getName())
                        .build();
            })).toList();
            return ResponseEntity.status(HttpStatus.OK).body(productResponseDtoList);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,  @RequestBody ProductRequestDto requestDto)
    {
        try
        {

            Product updatedProduct= productService.updateProduct(id,requestDto);

            ProductResponseDto responseDto= ProductResponseDto.builder()
                    .id(updatedProduct.getId())
                    .description(updatedProduct.getDescription())
                    .price(updatedProduct.getPrice())
                    .name(updatedProduct.getName())
                    .stockQuantity(updatedProduct.getStockQuantity())
                    .categoryName(updatedProduct.getCategory().getName())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
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
            if(productList==null || productList.isEmpty())
            {
                return   ResponseEntity.status(HttpStatus.OK).body("No result found for : "+search);
            }
            List<ProductResponseDto> productResponseDtoList=productList.stream().map((product -> {

                return ProductResponseDto
                        .builder()
                        .id(product.getId())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .name(product.getName())
                        .stockQuantity(product.getStockQuantity())
                        .categoryName(product.getCategory().getName())
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
