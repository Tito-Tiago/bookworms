package com.bookworms.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "respostas")
@Data
@NoArgsConstructor
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "avaliacao_id", nullable = false)
    private Avaliacao avaliacao;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @Column(nullable = false)
    private String comentario;

    @Column(nullable = false)
    private Integer likes = 0;

    public Resposta(Avaliacao avaliacao, Aluno aluno, String comentario) {
        this.avaliacao = avaliacao;
        this.aluno = aluno;
        this.comentario = comentario;
    }
}
