package com.DipYukti.Ecommerce.utility;

import com.DipYukti.Ecommerce.entity.CustomerUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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
        return Jwts
                .builder()
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .subject(customerUserDetails.getUsername())
                .claim("id: ",customerUserDetails.getCustomer().getId())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                .compact();
    }

    public String getUsernameFromJwtToken(String token)
    {
        Claims claims=Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();

    }
}
