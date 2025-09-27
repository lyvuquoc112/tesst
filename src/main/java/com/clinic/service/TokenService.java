package com.clinic.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    // Secret key cho JWT
    private final String SECRET = "ThisIsASecretKeyForJWTGeneration1234567890";
    private final Key signingKey = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * Generate JWT token using user's email
     * @param email user email
     * @return JWT token string
     */
    public String generateToken(String email) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + (1000 * 60 * 60); // token sống 1 giờ

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(expMillis))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Return signing key
     * @return signing key
     */
    public Key getSigningKey() {
        return signingKey;
    }
}
