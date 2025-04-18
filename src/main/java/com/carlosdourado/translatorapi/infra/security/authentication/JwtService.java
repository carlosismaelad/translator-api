package com.carlosdourado.translatorapi.infra.security.authentication;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationTime;
    private final JwtParser parser;

    public JwtService(JwtProperties properties){
        this.key = Keys.hmacShaKeyFor(properties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        this.expirationTime = properties.getExpirationTime();
        this.parser = Jwts.parser().verifyWith(key).build();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public String extracrUsername(String token){
        return parser.parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token){
        try{
            parser.parseSignedClaims(token);
            return true;
        }catch (Exception ex){
            return false;
        }
    }
}
