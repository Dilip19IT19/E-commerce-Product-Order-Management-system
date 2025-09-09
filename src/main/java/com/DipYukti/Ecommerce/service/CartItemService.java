package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.entity.CartItem;
import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.entity.Product;
import com.DipYukti.Ecommerce.repository.CartItemRepository;
import com.DipYukti.Ecommerce.repository.CustomerRepository;
import com.DipYukti.Ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final CacheManager cacheManager;

    @PreAuthorize("@customSecurity.hasPermissionAndIsSelf(#customerId,'CREATE_CART')")
    @CacheEvict(value = "cartItemsOfCustomer",key="#customerId")
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
            cart.setQuantity(cart.getQuantity()+quantity);
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


    @PreAuthorize("@customSecurity.hasPermissionAndIsSelf(#customerId,'UPDATE_CART')")
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
        Long customerId=cart.getCustomer().getId();

        var cache = cacheManager.getCache("cartItemsOfCustomer");
        if (cache != null) {
            cache.evict(customerId);
        }

        return cartItemRepository.save(cart);
    }

    @PreAuthorize("@customSecurity.hasPermissionAndIsSelf(#customerId,'DELETE_CART')")
    @Transactional
    public void removeItemFromCart(Long cartItemId)
    {
        CartItem cartItem=cartItemRepository.findById(cartItemId).orElse(null);
        if(cartItem!=null)
        {
            cartItemRepository.deleteById(cartItemId);

            var cache = cacheManager.getCache("cartItemsOfCustomer");
            if (cache != null) {
                cache.evict(cartItem.getCustomer().getId());
            }

        }
        else
        {
            throw  new EntityNotFoundException("cart does not exist with id: "+cartItemId);
        }
    }

    @PreAuthorize("@customSecurity.hasPermissionAndIsSelf(#customerId,'CLEAR_CART')")
    @CacheEvict(value = "cartItemsOfCustomer",key = "#customerId")
    @Transactional
    public void clearCart(Long customerId)
    {
        Customer customer=customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("Customer not found with id: "+customerId));
       cartItemRepository.deleteByCustomer(customer);
    }

    @PreAuthorize("@customSecurity.hasPermissionAndIsSelf(#customerId,'READ_CART')")
    @Cacheable(value = "cartItemsOfCustomer",key = "#customerId")
    @Transactional(readOnly = true)
    public List<CartItem> getCartItemsOfCustomer(Long customerId)
    {
        Customer customer=customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("Customer not found with id: "+customerId));
        return cartItemRepository.findByCustomer(customer);
    }
}
