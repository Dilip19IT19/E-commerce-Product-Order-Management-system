package com.DipYukti.Ecommerce.utility;


import com.DipYukti.Ecommerce.entity.CustomerUserDetails;
import com.DipYukti.Ecommerce.service.CustomerUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter
{
    private final CustomerUserDetailService customerUserDetailService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtAuthUtil authUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            String requestHeader=request.getHeader("Authorization");
            if(requestHeader==null || !requestHeader.startsWith("Bearer"))
            {
                filterChain.doFilter(request,response);
                return;
            }
            else
            {
                String token=requestHeader.split(" ")[1];
                String username= authUtil.getUsernameFromJwtToken(token);
                if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
                {
                    CustomerUserDetails customer=customerUserDetailService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(customer,null,customer.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
                filterChain.doFilter(request,response);
            }

        }
        catch (Exception e)
        {
            handlerExceptionResolver.resolveException(request,response,null,e);
            logger.error(e.getMessage());
        }
    }
}
