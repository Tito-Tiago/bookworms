package com.bookworms.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String sinopse;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false)
    private String fotoCapaUrl;

    @Column(nullable = false)
    private Integer likes = 0;

    @Column(nullable = false)
    private Integer somaAvaliacoes = 0;

    @Column(nullable = false)
    private Integer numAvaliacoes = 0;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    public Livro(String titulo, String sinopse, String genero, String autor) {
        this.titulo = titulo;
        this.sinopse = sinopse;
        this.genero = genero;
        this.autor = autor;
    }
}
