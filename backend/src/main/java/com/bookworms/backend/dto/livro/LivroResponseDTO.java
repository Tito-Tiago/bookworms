package com.bookworms.backend.dto.livro;

import com.bookworms.backend.dto.Avaliacao.AvaliacaoResponseDTO;
import com.bookworms.backend.model.Livro;

import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class LivroResponseDTO {
    private UUID id;
    private String titulo;
    private String sinopse;
    private String autor;
    private String genero;
    private String fotoCapaUrl;
    private Integer likes;
    private Double rating;
    private List<AvaliacaoResponseDTO> avaliacoes;

    public LivroResponseDTO(Livro livro) {
        this.id = livro.getId();
        this.titulo = livro.getTitulo();
        this.sinopse = livro.getSinopse();
        this.autor = livro.getAutor();
        this.genero = livro.getGenero();
        this.fotoCapaUrl = livro.getFotoCapaUrl();
        this.likes = livro.getLikes();
        this.rating = livro.getNumAvaliacoes() > 0 ? (double) livro.getSomaAvaliacoes() / livro.getNumAvaliacoes() : 0.0;
        this.avaliacoes = livro.getAvaliacoes().stream().map(AvaliacaoResponseDTO::new).collect(Collectors.toList());
    }
}
