package com.bookworms.backend.controller;

import com.bookworms.backend.dto.bibliotecario.BibliotecarioLoginRequestDTO;
import com.bookworms.backend.dto.aluno.LoginRequestDTO;
import com.bookworms.backend.dto.aluno.LoginResponseDTO;
import com.bookworms.backend.factory.ResponseFactory;
import com.bookworms.backend.response.ApiResponse;
import com.bookworms.backend.service.AlunoService;
import com.bookworms.backend.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AlunoService alunoService;
    private final TokenService tokenService;

    @Value("${app.bibliotecario.username}")
    private String bibliotecarioUsername;
    @Value("${app.bibliotecario.password}")
    private String bibliotecarioPassword;


    @PostMapping("/login/aluno")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> loginAluno(@RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO responseData = alunoService.login(dto);
        return ResponseFactory.success(responseData);
    }

    @PostMapping("/login/bibliotecario")
    public ResponseEntity<?> loginBibliotecario(@RequestBody BibliotecarioLoginRequestDTO dto) {
        if (bibliotecarioUsername.equals(dto.getUsername()) && bibliotecarioPassword.equals(dto.getPassword())) {
            String token = tokenService.generateBibliotecarioToken(dto.getUsername());
            LoginResponseDTO response = new LoginResponseDTO(token, null);
            return ResponseFactory.success(response);
        }

        return ResponseFactory.error(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.");
    }
}