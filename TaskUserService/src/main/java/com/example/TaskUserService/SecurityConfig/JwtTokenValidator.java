package com.example.TaskUserService.SecurityConfig;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER); // Ensure no non-breaking space after JWT_HEADER

        // Debugging headers
        System.out.println("Incoming headers: " + Collections.list(request.getHeaderNames()));

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Remove "Bearer " prefix
            try {
                Claims claims = JwtProvider.getClaims(jwt);
                String email = claims.get("email", String.class);
                String userId = claims.get("userId", String.class); // Extract userId from claims
                String authorities = claims.get("authorities", String.class);
                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Log extracted details for debugging
                System.out.println("Extracted email: " + email);
                System.out.println("Extracted userId: " + userId);
                System.out.println("Extracted authorities: " + authorities);

                // Set authentication context
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, userId, auth);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT Token: " + e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
