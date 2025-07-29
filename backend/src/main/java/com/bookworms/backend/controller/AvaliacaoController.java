package com.bookworms.backend.controller;

import com.bookworms.backend.dto.Avaliacao.RespostaCadastroDTO;
import com.bookworms.backend.dto.Avaliacao.RespostaResponseDTO;
import com.bookworms.backend.dto.Avaliacao.AvaliacaoCadastroDTO;
import com.bookworms.backend.dto.Avaliacao.AvaliacaoResponseDTO;
import com.bookworms.backend.factory.ResponseFactory;
import com.bookworms.backend.response.ApiResponse;
import com.bookworms.backend.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<ApiResponse<AvaliacaoResponseDTO>> createAvaliacao(@RequestBody @Valid AvaliacaoCadastroDTO dto) {
        AvaliacaoResponseDTO avaliacao = avaliacaoService.createAvaliacao(dto);
        return ResponseFactory.created(avaliacao);
    }

    @PostMapping("/respostas")
    public ResponseEntity<ApiResponse<RespostaResponseDTO>> createReply(@RequestBody @Valid RespostaCadastroDTO dto) {
        RespostaResponseDTO reply = avaliacaoService.createResposta(dto);
        return ResponseFactory.created(reply);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<AvaliacaoResponseDTO>> likeavAliacao(@PathVariable UUID id) {
        AvaliacaoResponseDTO avaliacao = avaliacaoService.likeAvaliacao(id);
        return ResponseFactory.success(avaliacao);
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<AvaliacaoResponseDTO>> unlikeAvaliacao(@PathVariable UUID id) {
        AvaliacaoResponseDTO avaliacao = avaliacaoService.unlikeAvaliacao(id);
        return ResponseFactory.success(avaliacao);
    }

    @PostMapping("/respostas/{id}/like")
    public ResponseEntity<ApiResponse<RespostaResponseDTO>> likeReply(@PathVariable UUID id) {
        RespostaResponseDTO reply = avaliacaoService.likeResposta(id);
        return ResponseFactory.success(reply);
    }

    @DeleteMapping("/respostas/{id}/like")
    public ResponseEntity<ApiResponse<RespostaResponseDTO>> unlikeReply(@PathVariable UUID id) {
        RespostaResponseDTO reply = avaliacaoService.unlikeResposta(id);
        return ResponseFactory.success(reply);
    }
}
