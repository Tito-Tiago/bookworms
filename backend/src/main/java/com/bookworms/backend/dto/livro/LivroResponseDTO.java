package com.bookworms.backend.dto.livro;

import com.bookworms.backend.model.Livro;

import lombok.Data;

import java.util.UUID;

@Data
public class LivroResponseDTO {
    private UUID id;
    private String titulo;
    private String sinopse;
    private String autor;
    private String genero;
    private String fotoCapaUrl;

    public LivroResponseDTO(Livro livro) {
        this.id = livro.getId();
        this.titulo = livro.getTitulo();
        this.sinopse = livro.getSinopse();
        this.autor = livro.getAutor();
        this.genero = livro.getGenero();
        this.fotoCapaUrl = livro.getFotoCapaUrl();
    }
}
