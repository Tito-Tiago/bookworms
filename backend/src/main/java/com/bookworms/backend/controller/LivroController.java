package com.bookworms.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.bookworms.backend.dto.livro.LivroCadastroDTO;
import com.bookworms.backend.dto.livro.LivroResponseDTO;
import com.bookworms.backend.factory.ResponseFactory;
import com.bookworms.backend.response.ApiResponse;
import com.bookworms.backend.service.LivroService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService service;

    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<LivroResponseDTO>> castrar (@RequestBody @Valid LivroCadastroDTO dto) {
        LivroResponseDTO livroCadastrado = service.cadastrarLivro(dto);
        return ResponseFactory.created(livroCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> buscarLivros(@RequestParam String param) {
        List<LivroResponseDTO> livros = service.getLivros();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> getLivroID(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getLivroById(id));
    }
    
    
}
