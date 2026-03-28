package com.example.urbanvibe.ecommerce.auth;

import com.example.urbanvibe.ecommerce.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    // Clave secrete larga
    @Value("${jwt.secret}")
    private String secretKey;

    // Generar un token para el usuario
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail()) // quién es el usuario
                .claim("role", user.getRole()) // info extra
                .issuedAt(new Date()) // fecha de creación
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(getSigningKey())
                .compact();
    }

    // Extraer el email del token
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validación de token correcto y sin expiración
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final  String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
