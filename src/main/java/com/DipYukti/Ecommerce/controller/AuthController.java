package com.DipYukti.Ecommerce.controller;

import com.DipYukti.Ecommerce.dto.LoginRequestDto;
import com.DipYukti.Ecommerce.dto.RefreshTokenRequestDto;
import com.DipYukti.Ecommerce.dto.RefreshTokenResponseDto;
import com.DipYukti.Ecommerce.dto.SignupRequestDto;
import com.DipYukti.Ecommerce.entity.RefreshToken;
import com.DipYukti.Ecommerce.service.AuthService;
import com.DipYukti.Ecommerce.service.CustomerUserDetailService;
import com.DipYukti.Ecommerce.service.RefreshTokenService;
import com.DipYukti.Ecommerce.utility.JwtAuthUtil;
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
    private final JwtAuthUtil authUtil;
    private final CustomerUserDetailService customerUserDetailService;
    private final RefreshTokenService refreshTokenService;

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

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDto requestDto)
    {
        String refreshToken= requestDto.getRefreshToken();
        RefreshToken oldRefreshToken=refreshTokenService.findByToken(refreshToken);
        String accessToken="";
        if(refreshTokenService.isRefreshTokenExpired(oldRefreshToken))
        {
            RefreshToken newRefreshToken=refreshTokenService.createRefreshToken(oldRefreshToken.getCustomer().getId());
            refreshTokenService.deleteByToken(oldRefreshToken);
             accessToken=authUtil.createAccessToken(customerUserDetailService.loadUserByUsername(newRefreshToken.getCustomer().getEmail()));
        }
        else
        {
             accessToken=authUtil.createAccessToken(customerUserDetailService.loadUserByUsername(oldRefreshToken.getCustomer().getEmail()));
        }

        RefreshTokenResponseDto responseDto= RefreshTokenResponseDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(oldRefreshToken.getToken())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);

    }

}
