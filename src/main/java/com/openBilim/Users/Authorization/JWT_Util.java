package com.openBilim.Users.Authorization;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JWT_Util {
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION = 86400000; // 24 часа

public static String createTokenWithClaims(String login,  String role, String name, String id) {
    return Jwts.builder()
            .setSubject(login)
            .claim("name", name)   
            .claim("id", id)   
            .claim("role", role)                    // кастомный claim
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 час
            .signWith(key)
            .compact();
}

        public static String validateAndGetUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.get("id", String.class);
    }

       public static String validate(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.toString();
    }

    
}
