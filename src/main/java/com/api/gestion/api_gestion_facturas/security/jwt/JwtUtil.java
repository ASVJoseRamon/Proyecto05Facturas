package com.api.gestion.api_gestion_facturas.security.jwt;

import java.security.Key;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtUtil {
    private Key secret;

    @PostConstruct
    protected void init() {
        byte[] apiKeySecretBytes = new byte[64];
        new SecureRandom().nextBytes(apiKeySecretBytes);
        secret = Keys.hmacShaKeyFor(apiKeySecretBytes);
    }
    
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T>T extractClaims(String token,  Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getEncoded())) // 1. Reemplaza a setSigningKey()
                .build()                    // 2. Construye el parser
                .parseSignedClaims(token)   // 3. Reemplaza a parseClaimsJws()
                .getPayload();              // 4. Reemplaza a getBody()
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims,username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims) // Reemplaza a setClaims()
                .subject(subject)    // Reemplaza a setSubject()
                .issuedAt(new Date(System.currentTimeMillis())) // Reemplaza a setIssuedAt()
                // Corrección del error de suma en la fecha de expiración (10 horas)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Reemplaza a setExpiration()
                .signWith(Keys.hmacShaKeyFor(secret.getEncoded())) // Reemplaza a signWith(SignatureAlgorithm, String)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
