package com.claudialamas.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;


@Service
public class JwtIssuer {

    private final Key key;
    private final String issuer;
    private final long expMinutes;

    public JwtIssuer(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.exp-minutes}") long expMinutes) {
        this.key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.issuer = issuer;
        this.expMinutes = expMinutes;
    }

    public String issue(String subject, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expMinutes * 60);

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                // Em Java 8 não há Map.of -> usa Collections.singletonMap
                .addClaims(Collections.singletonMap("scope", role))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
