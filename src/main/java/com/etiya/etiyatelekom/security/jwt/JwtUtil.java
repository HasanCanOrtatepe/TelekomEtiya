package com.etiya.etiyatelekom.security.jwt;

import com.etiya.etiyatelekom.common.enums.UserType;
import com.etiya.etiyatelekom.security.model.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret:mySecretKeyThatIsAtLeast256BitsLongForHS256AlgorithmSecurity}")
    private String secret;

    @Value("${jwt.expiration-ms:86400000}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(AuthenticatedUser user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("userType", user.getUserType().name())
                .claim("authorities", authorities)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public AuthenticatedUser parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String email = claims.getSubject();
            Long userId = claims.get("userId", Long.class);
            UserType userType = UserType.valueOf(claims.get("userType", String.class));
            List<String> authStrings = claims.get("authorities", List.class);

            List<GrantedAuthority> authorities = authStrings.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return AuthenticatedUser.fromJwt(userId, email, userType, authorities);
        } catch (ExpiredJwtException e) {
            throw new io.jsonwebtoken.security.SecurityException("JWT token has expired");
        } catch (MalformedJwtException | SignatureException e) {
            throw new io.jsonwebtoken.security.SecurityException("Invalid JWT token");
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
