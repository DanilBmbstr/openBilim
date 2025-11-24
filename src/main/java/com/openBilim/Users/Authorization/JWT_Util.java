package com.openBilim.Users.Authorization;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openBilim.LOGGER;

import com.openBilim.Users.UserDTO;


public class JWT_Util {



    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION = 3600000; // 24 часа

public static String createTokenWithClaims(String login,  String role, String name, String group, String id) {
    LOGGER.info("Создание Токена пользователя: " + id);
    return Jwts.builder()
            .setSubject(login)
            .claim("name", name)   
            .claim("group", group)   
            .claim("id", id)   
            .claim("role", role)                    // кастомный claim
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 1 час
            .signWith(key)
            .compact();

    
}

        public static String validateAndGetUserId(String token) {
            try{
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        

        LOGGER.info("Успешно валидирован пользователь " + claims.get("id", String.class));
        return claims.get("id", String.class);
        }
        catch(Exception e){LOGGER.warning( "Ошибка валидации: " + e.getMessage()); return null;}
    }

       public static String validate(String token) {
        try{
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        LOGGER.info("Успешно валидирован пользователь " + claims.get("id", String.class));
        return claims.toString();
        }
        catch(Exception e) {LOGGER.warning( "Ошибка валидации: " + e.getMessage()); return null;}
    }

    public static String getJsonUserData(String token){
        try{
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        LOGGER.info("Успешно валидирован пользователь " + claims.get("id", String.class));
        String name =  claims.get("name",String.class);
        String group =  claims.get("group",String.class);
        String id =  claims.get("id",String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = new UserDTO(name, id ,group);
        return objectMapper.writeValueAsString(userDTO);
        
        }
        catch(Exception e) {LOGGER.warning( "Ошибка валидации: " + e.getMessage()); return null;}
    }

    
}
