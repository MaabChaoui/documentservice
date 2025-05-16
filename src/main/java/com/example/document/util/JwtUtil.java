package com.example.document.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component  // ✅ This is mandatory for Spring to inject it
public class JwtUtil {

    private static final String SECRET = "VerySecretKey12345678901234567890";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
