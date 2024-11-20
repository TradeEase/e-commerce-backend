package com.example.TaskUserService.SecurityConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtProvider {

    // Secret key for signing JWT
    private static final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    private static final long JWT_EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    // Method to generate JWT token
    public static String generateToken(Authentication auth) {
        // Get authorities (roles) from authentication object
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        // Convert authorities to a comma-separated string
        String roles = String.join(",", authorities.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));
        
        // Generate the JWT token
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME)) // Set expiration to 1 day
                .claim("email", auth.getName()) // Add the username (email) as a claim
                .claim("authorities", roles) // Add authorities (roles) as a claim
                .signWith(key) // Sign the token with the secret key
                .compact(); // Return the token as a compact string
    }

    // Method to extract email from JWT token
    // public static String getEmailFromJwtToken(String jwt) {
    //     jwt = jwt.substring(7); // Remove "Bearer " prefix from token
    //     try {
    //         // Parse the JWT token and extract claims
    //         Claims claims = Jwts.parserBuilder()
    //                 .setSigningKey(key)
    //                 .build()
    //                 .parseClaimsJws(jwt) // Parse the JWT token
    //                 .getBody();
            
    //         // Get email from the claims
    //         String email = claims.get("email", String.class);
    //         System.out.println("Email extracted from JWT: " + email);
    //         return email;
    //     } catch (Exception e) {
    //         System.err.println("Error extracting email from JWT: " + e.getMessage());
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    public static String getEmailFromJwtToken(String jwt) {
        jwt = jwt.substring(7); // Remove "Bearer " prefix from token
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            
            String email = claims.get("email", String.class);
            System.out.println("Email extracted from JWT: " + email);
            return email;
        } catch (Exception e) {
            System.err.println("Error extracting email from JWT: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
}
