package com.DipYukti.Ecommerce.controller;

import com.DipYukti.Ecommerce.dto.OrderItemResponseDto;
import com.DipYukti.Ecommerce.dto.OrderRequestDto;
import com.DipYukti.Ecommerce.dto.OrderResponseDto;
import com.DipYukti.Ecommerce.dto.OrderUpdateResponseDto;
import com.DipYukti.Ecommerce.entity.Order;
import com.DipYukti.Ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController
{
    private final OrderService orderService;

    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderRequestDto requestDto)
    {
        try
        {
            Order order=orderService.placeOrder(requestDto.getCustomerId());
            List<OrderItemResponseDto> orderItemResponseDtoList=order.getItems().stream().map((orderItem ->{
              return  OrderItemResponseDto.builder()
                      .productId(orderItem.getProduct().getId())
                      .productName(orderItem.getProductName())
                      .price(orderItem.getPrice())
                      .quantity(orderItem.getQuantity())
                      .build();
            } )).toList();
            OrderResponseDto responseDto=OrderResponseDto
                    .builder()
                    .orderId(order.getId())
                    .totalAmount(order.getTotalAmount())
                    .customerId(order.getCustomer().getId())
                    .orderItems(orderItemResponseDtoList)
                    .orderStatus(order.getOrderStatus().name())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("customer/{customerId}")
    public ResponseEntity<?> getOrdersOfACustomer(@PathVariable Long customerId)
    {
        try
        {
            List<Order>orders=orderService.getOrdersByCustomer(customerId);
            List<OrderResponseDto> orderResponseDtoList=orders.stream().map((order -> {
                List<OrderItemResponseDto> orderItemResponseDtoList=order.getItems().stream().map((orderItem ->{
                    return  OrderItemResponseDto.builder()
                            .productId(orderItem.getProduct().getId())
                            .productName(orderItem.getProductName())
                            .price(orderItem.getPrice())
                            .quantity(orderItem.getQuantity())
                            .build();
                } )).toList();
                return OrderResponseDto
                        .builder()
                        .orderId(order.getId())
                        .customerId(order.getCustomer().getId())
                        .totalAmount(order.getTotalAmount())
                        .orderItems(orderItemResponseDtoList)
                        .orderStatus(order.getOrderStatus().name())
                        .build();

            })).toList();
            return ResponseEntity.status(HttpStatus.OK).body(orderResponseDtoList);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public ResponseEntity<?> getOrderById(@PathVariable Long orderId)
    {
        try
        {
            Order order=orderService.getOrderById(orderId);
            List<OrderItemResponseDto> orderItemResponseDtoList=order.getItems().stream().map((orderItem ->{
                return  OrderItemResponseDto.builder()
                        .productId(orderItem.getProduct().getId())
                        .productName(orderItem.getProductName())
                        .price(orderItem.getPrice())
                        .quantity(orderItem.getQuantity())
                        .build();
            } )).toList();
            OrderResponseDto responseDto=OrderResponseDto
                    .builder()
                    .orderId(order.getId())
                    .totalAmount(order.getTotalAmount())
                    .orderItems(orderItemResponseDtoList)
                    .orderStatus(order.getOrderStatus().name())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrderStatus(@RequestParam Long orderId,@RequestParam String orderStatus )
    {
        try
        {
           Order order= orderService.updateOrderStatus(orderId, orderStatus);
            OrderUpdateResponseDto responseDto=OrderUpdateResponseDto
                    .builder()
                    .orderId(order.getId())
                    .orderStatus(order.getOrderStatus().name())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId)
    {
        try
        {
            orderService.cancelOrder(orderId);
            return ResponseEntity.status(HttpStatus.OK).body("Order cancel successfully.");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> permanentlyDeleteOrder(@PathVariable Long orderId)
    {
        try
        {
            orderService.deleteOrder(orderId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Order deleted successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
