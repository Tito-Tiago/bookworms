package com.bookworms.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
@Getter
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

    public Livro(String titulo, String sinopse, String genero, String autor) {
        this.titulo = titulo;
        this.sinopse = sinopse;
        this.genero = genero;
        this.autor = autor;
    }
}
