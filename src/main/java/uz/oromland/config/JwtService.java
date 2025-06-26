package uz.oromland.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${oromland.jwt.secret}")
    private String SECRET_KEY;

    private Key getSigningKey() {
        try {
            return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error creating signing key: " + e.getMessage());
            // Return a default key for development purposes
            // In production, this should throw an exception or use a fallback mechanism
            return Keys.hmacShaKeyFor("fallback_secret_key_for_development_only".getBytes());
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error parsing JWT token: " + e.getMessage());
            // Return empty claims
            return Jwts.claims();
        }
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 10 soat
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String userDetails) {
        try {
            String username = extractUsername(token);
            return username != null && username.equals(userDetails) && !isTokenExpired(token);
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error validating token: " + e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error checking token expiration: " + e.getMessage());
            return true; // Consider expired if there's an error
        }
    }
}
