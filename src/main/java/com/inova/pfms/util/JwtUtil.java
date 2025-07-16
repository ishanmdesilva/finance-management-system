package com.inova.pfms.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;


import io.jsonwebtoken.JwtParser;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    // Must be at least 32 characters (256 bits) for HS256
	//Avoid hardcoding. Instead, use: System.getenv("JWT_SECRET")
    private static final String SECRET = "mySecretKeymySecretKeymySecretKeymy";
    private final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 hours

    
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final JwtParser parser = Jwts.parser().verifyWith(key).build();

    public String generateToken(String email, String role) {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }


    public String extractEmail(String token) {
        Claims claims = parser.parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public String extractRole(String token) {
        Claims claims = parser.parseSignedClaims(token).getPayload();
        return claims.get("role", String.class);
    }

    public boolean isTokenValid(String token, String username) {
        return extractEmail(token).equals(username) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = parser.parseSignedClaims(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }

}
