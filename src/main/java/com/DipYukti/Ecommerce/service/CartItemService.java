package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.entity.CartItem;
import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.entity.Product;
import com.DipYukti.Ecommerce.repository.CartItemRepository;
import com.DipYukti.Ecommerce.repository.CustomerRepository;
import com.DipYukti.Ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService
{
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartItem addIemToCart(Long customerId, Long productId, Integer quantity)
    {
        Customer customer=customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("Customer not found with id: "+customerId));
        Product product=productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException("Product not found with id: "+productId));
        if(quantity > product.getStockQuantity())
        {
            throw new IllegalArgumentException("Not have enough stock of this product");
        }
        // if the cart item is already present then just update quantity
        CartItem cart=cartItemRepository.findByProductAndCustomer(product,customer).orElse(null);
        if(cart!=null)
        {
            cart.setQuantity(quantity);
            return cartItemRepository.save(cart);
        }
        else
        {
            return cartItemRepository.save(CartItem
                    .builder()
                    .product(product)
                    .customer(customer)
                    .quantity(quantity)
                    .build());
        }

    }


    @Transactional
    public CartItem updateCartItem(Long cartItemId, int quantity)
    {
        if(quantity<=0)
        {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        CartItem cart=cartItemRepository.findById(cartItemId).orElseThrow(()->new EntityNotFoundException("CartItem not found with id: "+cartItemId));
        if(cart.getProduct().getStockQuantity()<quantity)
        {
            throw new IllegalArgumentException("Not have enough stock of this product");
        }
        cart.setQuantity(quantity);
        return cartItemRepository.save(cart);
    }

    @Transactional
    public void removeItemFromCart(Long cartItemId)
    {
        if(cartItemRepository.existsById(cartItemId))
        {
            cartItemRepository.deleteById(cartItemId);
        }
        else
        {
            throw  new EntityNotFoundException("cart does not exist with id: "+cartItemId);
        }
    }

    @Transactional
    public void clearCart(Long customerId)
    {
        Customer customer=customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("Customer not found with id: "+customerId));
       cartItemRepository.deleteByCustomer(customer);
    }

    @Transactional(readOnly = true)
    public List<CartItem> getCartItemsOfCustomer(Long customerId)
    {
        Customer customer=customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("Customer not found with id: "+customerId));
        return cartItemRepository.findByCustomer(customer);
    }
}
