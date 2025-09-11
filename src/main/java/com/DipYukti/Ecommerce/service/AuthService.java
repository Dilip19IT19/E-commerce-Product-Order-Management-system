package com.DipYukti.Ecommerce.service;

import com.DipYukti.Ecommerce.dto.LoginRequestDto;
import com.DipYukti.Ecommerce.dto.LoginResponseDto;
import com.DipYukti.Ecommerce.dto.SignupRequestDto;
import com.DipYukti.Ecommerce.dto.SignupResponseDto;
import com.DipYukti.Ecommerce.entity.Customer;
import com.DipYukti.Ecommerce.entity.CustomerUserDetails;
import com.DipYukti.Ecommerce.entity.RefreshToken;
import com.DipYukti.Ecommerce.exceptions.UserAlreadyExistsException;
import com.DipYukti.Ecommerce.repository.CustomerRepository;
import com.DipYukti.Ecommerce.type.RoleType;
import com.DipYukti.Ecommerce.utility.JwtAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService
{
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthUtil authUtil;
    private final RefreshTokenService refreshTokenService;



    @Transactional
    public SignupResponseDto registerCustomer(SignupRequestDto requestDto)
    {
        Customer customer= customerRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if(customer!=null)
        {
            throw new UserAlreadyExistsException("User/Email: "+requestDto.getEmail()+" already exists");
        }
        else
        {

            customer= Customer
                    .builder()
                    .name(requestDto.getName())
                    .email(requestDto.getEmail())
                    .password(passwordEncoder.encode(requestDto.getPassword()))
                    .address(requestDto.getAddress())
                    .role(RoleType.ROLE_USER)
                    .build();
            customer=customerRepository.save(customer);
            return SignupResponseDto
                    .builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .email(customer.getEmail())
                    .address(customer.getAddress())
                    .build();
        }

    }

    @Transactional
    public LoginResponseDto loginCustomer(LoginRequestDto requestDto)
    {
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(),requestDto.getPassword()));
        CustomerUserDetails customerUserDetails=(CustomerUserDetails) authentication.getPrincipal();
        String accessToken=authUtil.createAccessToken(customerUserDetails);
        RefreshToken refreshToken=refreshTokenService.createRefreshToken(customerUserDetails.getCustomer().getId());
        return LoginResponseDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .id(customerUserDetails.getCustomer().getId())
                .username(customerUserDetails.getUsername())
                .build();
    }
}
