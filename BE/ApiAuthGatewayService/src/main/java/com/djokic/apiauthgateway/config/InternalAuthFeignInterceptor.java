package com.djokic.apiauthgateway.config;

import com.djokic.apiauthgateway.service.JwtService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
@Component
public class InternalAuthFeignInterceptor implements RequestInterceptor {

    private final JwtService jwtService;

    @Value("${AUTH_SHARED_SECRET}")
    private String sharedSecret;

    public InternalAuthFeignInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void apply(RequestTemplate template) {
        String path1 = template.path();
        if(path1.startsWith("/users/login") || path1.startsWith("/users/register")) {
            return; // ne dodaj JWT
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("No JWT token found in request");
        }

        String jwt = authHeader.substring(7);
        String userId = jwtService.extractClaim(jwt, claims -> claims.get("id").toString());
        String roleWithPrefix = jwtService.extractClaim(jwt, claims -> claims.get("role").toString());
        String platformRole = roleWithPrefix.replaceFirst("^ROLE_", ""); // ukloni ROLE_ prefiks

        long timestamp = Instant.now().getEpochSecond();

        String method = template.method();
        String path = template.path();
        String query = template.queryLine() != null ? template.queryLine() : "";

        String canonical = String.join("\n", method, path, query, userId, platformRole, String.valueOf(timestamp));
        String signature = hmacSha256Base64Url(sharedSecret, canonical);

        template.header("X-User-Id", userId);
        template.header("X-Platform-Role", platformRole); // bez ROLE_ prefiksa
        template.header("X-Timestamp", String.valueOf(timestamp));
        template.header("X-Signature-V", "v1");
        template.header("X-Signature", signature);
        template.header("X-Method", method);
        template.header("X-Path", path);
        template.header("X-Query", query);
    }

    private String hmacSha256Base64Url(String secret, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute HMAC signature", e);
        }
    }
}