package com.bookworms.backend.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bookworms.backend.model.Aluno;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;


@Service
public class TokenService {

    @Value("${JWT_SECRET}")
    private String secret;


    public String generateToken(Aluno aluno) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("bookworms-api")
                    .withSubject(aluno.getUsername())
                    .withClaim("roles", List.of("ROLE_ALUNO"))
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT para aluno", exception);
        }
    }

    public String generateBibliotecarioToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("bookworms-api")
                    .withSubject(username)
                    .withClaim("roles", List.of("ROLE_BIBLIOTECARIO"))
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT para bibliotecário", exception);
        }
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("bookworms-api")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}