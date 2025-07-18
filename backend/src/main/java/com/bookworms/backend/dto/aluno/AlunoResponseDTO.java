package com.bookworms.backend.dto.aluno;

import com.bookworms.backend.model.Aluno;
import lombok.Data;

import java.util.UUID;

@Data
public class AlunoResponseDTO {
    private UUID id;
    private String nomeCompleto;
    private String email;
    private String username;
    private String fotoPerfilUrl;

    public AlunoResponseDTO(Aluno aluno) {
        this.id = aluno.getId();
        this.nomeCompleto = aluno.getNomeCompleto();
        this.email = aluno.getEmail();
        this.username = aluno.getUsername();
        this.fotoPerfilUrl = aluno.getFotoPerfilUrl();
    }
}