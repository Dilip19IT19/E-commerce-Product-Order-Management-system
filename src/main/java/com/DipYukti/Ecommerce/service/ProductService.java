package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.dto.ProductRequestDto;
import com.DipYukti.Ecommerce.entity.Product;
import com.DipYukti.Ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;

    public Product createProduct(Product product)
    {
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long productId)
    {
        return productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException("No product found with this id: "+productId));
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    //TODO: Implement getAllPaginatedProducts()

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryId(Long categoryId)
    {
        return productRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProductsByName(String name)
    {
        return productRepository.findByNameIgnoringCase(name);
    }

    @Transactional
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
