package com.DipYukti.Ecommerce.controller;

import com.DipYukti.Ecommerce.dto.LoginRequestDto;
import com.DipYukti.Ecommerce.dto.SignupRequestDto;
import com.DipYukti.Ecommerce.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequestDto requestDto)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerCustomer(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto requestDto)
    {
        return ResponseEntity.status(HttpStatus.OK).body(authService.loginCustomer(requestDto));
    }

}
