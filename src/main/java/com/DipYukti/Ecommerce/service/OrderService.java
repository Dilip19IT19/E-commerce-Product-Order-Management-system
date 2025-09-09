package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.entity.*;
import com.DipYukti.Ecommerce.repository.CartItemRepository;
import com.DipYukti.Ecommerce.repository.CustomerRepository;
import com.DipYukti.Ecommerce.repository.OrderRepository;
import com.DipYukti.Ecommerce.repository.ProductRepository;
import com.DipYukti.Ecommerce.type.StatusType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService
{
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final CacheManager cacheManager;

    @PreAuthorize("@customSecurity.hasPermissionAndIsSelf(#customerId,'CREATE_ORDER')")
    @CacheEvict(value = "ordersByCustomer",key = "#customerId")
    @Transactional
    public Order placeOrder(Long customerId)
    {
        Customer customer=customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("Customer not found with id: "+customerId));
        List<CartItem> cartItems=cartItemRepository.findByCustomer(customer);
        if(cartItems.isEmpty())
        {
            throw  new IllegalArgumentException("No items found in cart");
                                    }
        List<OrderItem>orderItems= cartItems.stream().map(item->{
            Product product=item.getProduct();
            if(product.getStockQuantity()<item.getQuantity())
            {
                throw  new IllegalArgumentException("Not have enough stock");
            }
            product.setStockQuantity(product.getStockQuantity()-item.getQuantity());

            OrderItem orderItem= OrderItem.builder()
                    .product(item.getProduct())
                    .productName(item.getProduct().getName())
                    .quantity(item.getQuantity())
                    .price(item.getProduct().getPrice())
                    .build();
            product.getItems().add(orderItem);
            productRepository.save(product); // update product after stock quantity changes
            return orderItem;
        }
        ).toList();
        BigDecimal totalAmount=orderItems.stream()
                .map(orderItem -> orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        Order order=Order.builder()
                .totalAmount(totalAmount)
                .customer(customer)
                .items(orderItems)
                .orderDate(LocalDateTime.now())
                .orderStatus(StatusType.PLACED)
                .build();
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        cartItemRepository.deleteByCustomer(customer);
        return orderRepository.save(order);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional(readOnly = true)
    @Cacheable(value = "orders",key = "#orderId")
    public Order getOrderById(Long orderId)
    {
        return orderRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("Order not found with id: "+orderId));

    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional(readOnly = true)
    @Cacheable(value = "ordersByCustomer",key = "#customerId")
    public List<Order> getOrdersByCustomer(Long customerId)
    {
        Customer customer=customerRepository.findById(customerId).orElseThrow(()->new EntityNotFoundException("Customer not found with id: "+customerId));

        return orderRepository.findByCustomer(customer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CachePut(value = "orders",key = "#orderId")
    @Transactional
    public Order updateOrderStatus(Long orderId, String status)
    {
        Order order= orderRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("Order not found with id: "+orderId));
        StatusType newStatusType;
        try
        {
            newStatusType= StatusType.valueOf(status.toUpperCase());
        }
        catch (IllegalArgumentException ex)
        {
            throw  new IllegalArgumentException("Not a valid status type "+status);
        }
        order.setOrderStatus(newStatusType);
        Order updatedOrder= orderRepository.save(order);

        var cache= cacheManager.getCache("ordersByCustomer");
        if(cache!=null)
        {
            cache.evict(order.getCustomer().getId());
        }

        return updatedOrder;

    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @CachePut(value = "orders",key = "#orderId")
    @Transactional
    public Order cancelOrder(Long orderId)
    {
        Order order= orderRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("Order not found with id: "+orderId));
        StatusType orderStatus=order.getOrderStatus();
        if(orderStatus==StatusType.SHIPPED || orderStatus==StatusType.DELIVERED)
        {
            throw new  IllegalArgumentException("Order can't be cancelled since it is already "+orderStatus.name());
        }
        order.getItems().forEach(orderItem -> {
            Product product=orderItem.getProduct();
            product.setStockQuantity(product.getStockQuantity()+orderItem.getQuantity());
            productRepository.save(product);
        });
        order.setOrderStatus(StatusType.CANCELLED);

        var cache= cacheManager.getCache("ordersByCustomer");
        if(cache!=null)
        {
            cache.evict(order.getCustomer().getId());
        }

         return orderRepository.save(order);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "orders",key = "#orderId")
    @Transactional
    public void deleteOrder(Long orderId)
    {
        Order order= orderRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("Order not found with id: "+orderId));
        if(order.getOrderStatus()!=StatusType.CANCELLED)
        {
            throw new IllegalArgumentException("Only cancelled order can be deleted");
        }
        else
        {
            orderRepository.deleteById(orderId);

            var cache= cacheManager.getCache("ordersByCustomer");
            if(cache!=null)
            {
                cache.evict(order.getCustomer().getId());
            }

        }
    }
}
