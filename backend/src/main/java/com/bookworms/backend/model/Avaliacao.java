package com.bookworms.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "avaliacoes")
@Data
@NoArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(nullable = false)
    private Integer numEstrelas;

    @Column
    private String comentario;

    @Column(nullable = false)
    private Integer likes = 0;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas = new ArrayList<>();

    public Avaliacao(Livro livro, Aluno aluno, Integer numEstrelas, String comentario) {
        this.livro = livro;
        this.aluno = aluno;
        this.numEstrelas = numEstrelas;
        this.comentario = comentario;
    }
}
