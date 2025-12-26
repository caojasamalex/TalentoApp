package com.djokic.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InternalAuthFilter extends OncePerRequestFilter {
    @Value("${AUTH_SHARED_SECRET}")
    private String sharedSecret;

    @Value("${internal.auth.allowed-skew-seconds}")
    private int skewSeconds;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if("OPTIONS".equalsIgnoreCase(method)
                || uri.startsWith("/users/register")
                || uri.startsWith("/users/login")
                || uri.startsWith("/actuator/health")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-Platform-Role");
        String timestamp = request.getHeader("X-Timestamp");
        String signatureVersion = Optional.ofNullable(request.getHeader("X-Signature-V")).orElse("v1");
        String signature = request.getHeader("X-Signature");

        if(userId == null || role == null || timestamp == null || signature == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing required internal auth headers!");
            return;
        }

        if(!"v1".equals(signatureVersion)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unsupported signature version!");
            return;
        }

        long now = Instant.now().getEpochSecond();
        long timestampSeconds;
        try { timestampSeconds = Long.parseLong(timestamp); } catch (NumberFormatException e) { timestampSeconds = 0; }

        if(timestampSeconds == 0 || Math.abs(now - timestampSeconds) > skewSeconds) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Stale or invalid timestamp!");
            return;
        }

        String signedMethod = Optional.ofNullable(request.getHeader("X-Method")).orElse(request.getMethod());
        String signedPath   = Optional.ofNullable(request.getHeader("X-Path")).orElse(request.getRequestURI());
        String signedQuery  = Optional.ofNullable(request.getHeader("X-Query")).orElse(Optional.ofNullable(request.getQueryString()).orElse(""));

        String canonical = String.join("\n", signedMethod, signedPath, signedQuery, userId, role, timestamp);
        String expected = hmacSha256Base64Url(sharedSecret, canonical);

        if(!constantTimeEquals(expected, signature)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid signature!");
            return;
        }

        List<GrantedAuthority> authorities = Arrays.stream(role.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(r -> r.startsWith("ROLE_") ? r : ("ROLE_" + r))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    private boolean constantTimeEquals(String a, String b) {
        if(a == null || b == null){
            return false;
        }

        byte[] ba = a.getBytes(StandardCharsets.UTF_8);
        byte[] bb = b.getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(ba, bb);
    }

    private String hmacSha256Base64Url(String sharedSecret, String canonical) {
        if(sharedSecret == null){
            throw new IllegalStateException("internal.auth.shared-secret is not configured!");
        }

        try{
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(sharedSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signatureBytes = mac.doFinal(canonical.getBytes(StandardCharsets.UTF_8));

            return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute internal signature", e);
        }
    }
}
