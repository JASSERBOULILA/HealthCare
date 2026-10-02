package com.healthcareapp.healthcareapp.services;

import com.healthcareapp.healthcareapp.security.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;
    @Getter
    private final String cookieName;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expirationMs,
                      @Value("${jwt.cookie-name}") String cookieName) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMs = expirationMs;
        this.cookieName = cookieName;
    }

    public String generateToken(UserPrincipal user) {
        Date now = new Date();
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }

    /** Throws JwtException if the token is expired, tampered with, or malformed. */
    public String extractEmail(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public ResponseCookie createCookie(String token) {
        return ResponseCookie.from(cookieName, token)
                .httpOnly(true)             // JS cannot read it
                .secure(false)       // HTTPS only in production
                .sameSite("Lax")            // blocks cross-site POST/DELETE (CSRF)
                .path("/")
                .maxAge(Duration.ofMillis(expirationMs))
                .build();
    }

    public ResponseCookie clearCookie() {
        return ResponseCookie.from(cookieName, "")
                .httpOnly(true).secure(false).sameSite("Lax")
                .path("/").maxAge(0).build();
    }
}
