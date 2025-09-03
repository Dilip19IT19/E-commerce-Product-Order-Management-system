package com.DipYukti.Ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(name = "customers")
public class Customer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotBlank(message = "customer name is required")
    private String name;
    @Column(unique = true,nullable = false)
    @Email
    private String email;
    private String address;
    @NotBlank
    @Size(min = 3, max = 15, message = "password must have length between 3 and 15")
    private String password;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Order> orders;
}
