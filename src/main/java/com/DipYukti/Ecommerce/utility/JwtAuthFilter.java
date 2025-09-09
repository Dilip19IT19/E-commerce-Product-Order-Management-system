package com.DipYukti.Ecommerce.utility;


import com.DipYukti.Ecommerce.entity.CustomerUserDetails;
import com.DipYukti.Ecommerce.service.CustomerUserDetailService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                Claims claims = authUtil.getClaimsFromJwtToken(token);
                String username=claims.getSubject();
                if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
                {
                    CustomerUserDetails customer=customerUserDetailService.loadUserByUsername(username);
                    List<String> permissions=claims.get("permissions",List.class);
                    List<GrantedAuthority> authorities=new ArrayList<>(customer.getAuthorities());
                    authorities.addAll(permissions.stream().map(p->new SimpleGrantedAuthority(p)).toList());
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(customer,null,authorities);
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
