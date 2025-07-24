package com.bookworms.backend.controller;

import com.bookworms.backend.dto.livro.LivroCadastroDTO;
import com.bookworms.backend.dto.livro.LivroResponseDTO;
import com.bookworms.backend.factory.ResponseFactory;
import com.bookworms.backend.response.ApiResponse;
import com.bookworms.backend.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService service;

    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<LivroResponseDTO>> cadastrar(@RequestBody @Valid LivroCadastroDTO dto) {
        LivroResponseDTO livroCadastrado = service.cadastrarLivro(dto);
        return ResponseFactory.created(livroCadastrado);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LivroResponseDTO>>> buscarLivros() {
        List<LivroResponseDTO> livros = service.getLivros();
        return ResponseFactory.success(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LivroResponseDTO>> getLivroID(@PathVariable UUID id) {
        return ResponseFactory.success(service.getLivroById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deletar(@PathVariable UUID id) {
        service.deletarLivro(id);
        Map<String, String> responseData = Map.of("mensagem", "Livro excluído com sucesso.");
        return ResponseFactory.success(responseData);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<LivroResponseDTO>> like(@PathVariable UUID id) {
        return ResponseFactory.success(service.like(id));
    }
}
