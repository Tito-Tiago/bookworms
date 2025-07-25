package com.bookworms.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "alunos")
@Data
@NoArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String senha;

    @Column(name = "foto_perfil_url")
    private String fotoPerfilUrl;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    

    public Aluno(String nomeCompleto, String email, String username, String senha) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.username = username;
        this.senha = senha;
    }
}