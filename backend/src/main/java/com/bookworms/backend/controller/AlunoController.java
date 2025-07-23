package com.bookworms.backend.controller;

import com.bookworms.backend.dto.aluno.*;
import com.bookworms.backend.factory.ResponseFactory;
import com.bookworms.backend.response.ApiResponse;
import com.bookworms.backend.service.AlunoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

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

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoResponseDTO>> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid AlunoUpdateDTO dto) {

        AlunoResponseDTO alunoAtualizado = alunoService.atualizarAluno(id, dto);
        return ResponseFactory.success(alunoAtualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoResponseDTO>> visualizar(@PathVariable UUID id) {
        AlunoResponseDTO aluno = alunoService.buscarAlunoPorId(id);
        return ResponseFactory.success(aluno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> excluir(@PathVariable UUID id) {
        alunoService.excluirAluno(id);

        Map<String, String> responseData = Map.of("mensagem", "Aluno excluído com sucesso.");

        return ResponseFactory.success(responseData);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO responseData = alunoService.login(dto);

        return ResponseFactory.success(responseData);
    }
}