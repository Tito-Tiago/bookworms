package com.bookworms.backend.controller;

import com.bookworms.backend.dto.aluno.AlunoCadastroDTO;
import com.bookworms.backend.dto.aluno.AlunoResponseDTO;
import com.bookworms.backend.factory.ResponseFactory;
import com.bookworms.backend.response.ApiResponse;
import com.bookworms.backend.service.AlunoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<AlunoResponseDTO>> cadastrar(@RequestBody @Valid AlunoCadastroDTO dto) {
        AlunoResponseDTO alunoCadastrado = alunoService.cadastrarAluno(dto);
        return ResponseFactory.created(alunoCadastrado);
    }
}