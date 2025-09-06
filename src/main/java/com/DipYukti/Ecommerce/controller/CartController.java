package com.DipYukti.Ecommerce.controller;

import com.DipYukti.Ecommerce.dto.CartItemRequestDto;
import com.DipYukti.Ecommerce.dto.CartItemResponseDto;
import com.DipYukti.Ecommerce.dto.CartResponseDto;
import com.DipYukti.Ecommerce.entity.CartItem;
import com.DipYukti.Ecommerce.service.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController
{
    private final CartItemService cartItemService;

    @PostMapping("/items")
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody CartItemRequestDto requestDto)
    {
        try
        {

            CartItem cartItem= cartItemService.addIemToCart(requestDto.getCustomerId(), requestDto.getProductId(), requestDto.getQuantity());
            CartItemResponseDto responseDto=CartItemResponseDto
                    .builder()
                    .cartItemId(cartItem.getId())
                    .price(cartItem.getProduct().getPrice())
                    .productName(cartItem.getProduct().getName())
                    .quantity(cartItem.getQuantity())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("customer/{customerId}")
    public ResponseEntity<?> getCarts(@PathVariable Long customerId)
    {
        try
        {
            List<CartItem> cartItemList= cartItemService.getCartItemsOfCustomer(customerId);

            List<CartItemResponseDto> responseDtoList=cartItemList.stream().map((cartItem -> {

                return CartItemResponseDto.builder()
                        .price(cartItem.getProduct().getPrice())
                        .productName(cartItem.getProduct().getName())
                        .quantity(cartItem.getQuantity())
                        .cartItemId(cartItem.getId())
                        .build();
            })).toList();

            BigDecimal totalAmount=cartItemList.stream()
                    .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                    .reduce(BigDecimal.ZERO,BigDecimal::add);

            CartResponseDto responseDto= CartResponseDto
                    .builder()
                    .cartItems(responseDtoList)
                    .customerId(customerId)
                    .totalPrice(totalAmount)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCartItemQuantity(@RequestParam Long cartItemId, @RequestParam Integer quantity)
    {
        try
        {
            CartItem cartItem=cartItemService.updateCartItem(cartItemId,quantity);
            CartItemResponseDto responseDto=CartItemResponseDto
                    .builder()
                    .cartItemId(cartItem.getId())
                    .price(cartItem.getProduct().getPrice())
                    .productName(cartItem.getProduct().getName())
                    .quantity(cartItem.getQuantity())
                    .build();

            return   ResponseEntity.status(HttpStatus.OK).body(responseDto);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/cancel/{cartItemId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long cartItemId)
    {
        try
        {
            cartItemService.removeItemFromCart(cartItemId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Item remove from cart successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("customer/{customerId}")
    public ResponseEntity<?> clearCartsOfCustomer(@PathVariable Long customerId)
    {
        try
        {
            cartItemService.clearCart(customerId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("All cart items removed of customer : "+customerId);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
