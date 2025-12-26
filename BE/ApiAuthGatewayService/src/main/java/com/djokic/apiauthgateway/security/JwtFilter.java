package com.djokic.apiauthgateway.security;

import com.djokic.apiauthgateway.service.AuthService;
import com.djokic.apiauthgateway.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.debug("JwtFilter triggered for URI: {}", request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        log.debug("Authorization header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No Bearer token found, continuing filter chain without authentication");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        log.debug("Extracted JWT: {}", jwt);

        try {
            final String userEmail = jwtService.extractUsername(jwt);
            final String role = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));

            log.debug("Extracted userEmail from JWT: {}", userEmail);
            log.debug("Extracted role from JWT: {}", role);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("SecurityContext is empty, creating UserDetails and AuthenticationToken");

                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(userEmail)
                        .password("") // Password nam ne treba za SecurityContext
                        .authorities(role != null ? role : "ROLE_PLATFORM_USER")
                        .build();

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.debug("Authentication set in SecurityContextHolder for user: {}", userEmail);
                } else {
                    log.debug("JWT is invalid for user: {}", userEmail);
                }
            } else {
                log.debug("UserEmail is null or SecurityContext already has authentication");
            }
        } catch (Exception e) {
            log.error("Error in JwtFilter processing", e);
        }

        filterChain.doFilter(request, response);
    }

}