package com.rodolfoafonso.nobile.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.rodolfoafonso.nobile.domain.entity.PasswordResetToken;
import com.rodolfoafonso.nobile.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class PasswordResetTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateResetToken(String email) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withIssuer("auth-api")
                .withSubject(email)
                .withExpiresAt(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")))
                .sign(algorithm);

        PasswordResetToken entity = new PasswordResetToken();
        entity.setToken(token);
        entity.setEmail(email);
        entity.setExpiresAt(LocalDateTime.now().plusHours(2));
        entity.setUsed(false);

        tokenRepository.save(entity);
        return token;
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String email = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

            PasswordResetToken entity = tokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Token não encontrado."));

            if (entity.isUsed() || entity.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Token expirado ou já utilizado.");
            }

            return email;
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    public void invalidateToken(String token) {
        tokenRepository.findByToken(token).ifPresent(t -> {
            t.setUsed(true);
            tokenRepository.save(t);
        });
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

