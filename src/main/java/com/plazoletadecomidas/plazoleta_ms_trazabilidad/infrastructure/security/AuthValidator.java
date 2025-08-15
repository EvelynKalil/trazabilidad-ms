package com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.security;

import com.plazoletadecomidas.plazoleta_ms_trazabilidad.domain.model.Role;
import com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.UUID;

@Component
public class AuthValidator {

    private final JwtUtil jwtUtil;

    public AuthValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public UUID validate(String tokenHeader, Role requiredRole) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token ausente o mal formado");
        }

        String token = tokenHeader.replace("Bearer ", "");
        SecretKey secretKey = jwtUtil.getSecretKey();

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            // Solo validar si requiredRole no es null
            if (requiredRole != null && !requiredRole.name().equals(role)) {
                throw new UnauthorizedException("No tienes permisos para esta acción");
            }

            return UUID.fromString(userId);

        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("Token inválido o expirado");
        }
    }


    public String getRoleFromToken(String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token ausente o mal formado");
        }
        String token = tokenHeader.replace("Bearer ", "");
        SecretKey secretKey = jwtUtil.getSecretKey();

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("role", String.class);

        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("Token inválido o expirado");
        }
    }


}
