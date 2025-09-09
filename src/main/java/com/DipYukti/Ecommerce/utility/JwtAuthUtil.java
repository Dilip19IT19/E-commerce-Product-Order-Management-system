package com.DipYukti.Ecommerce.utility;

import com.DipYukti.Ecommerce.entity.CustomerUserDetails;
import com.DipYukti.Ecommerce.type.PermissionType;
import com.DipYukti.Ecommerce.type.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthUtil
{
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    public SecretKey getSecretKey()
    {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(CustomerUserDetails customerUserDetails)
    {
        RoleType role=customerUserDetails.getCustomer().getRole();
        List<PermissionType>permissionList=RolePermissions.getPermissionsByRole(role);
        return Jwts
                .builder()
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .subject(customerUserDetails.getUsername())
                .claim("id",customerUserDetails.getCustomer().getId())
                .claim("role",role.name())
                .claim("permissions",permissionList.stream().map(p->p.name()).toList())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                .compact();
    }

    public Claims getClaimsFromJwtToken(String token)
    {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();


    }
}
