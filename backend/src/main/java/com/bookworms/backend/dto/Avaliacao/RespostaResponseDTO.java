package com.bookworms.backend.dto.Avaliacao;

import com.bookworms.backend.dto.aluno.AlunoResponseDTO;
import com.bookworms.backend.model.Resposta;
import lombok.Data;

import java.util.UUID;

@Data
public class RespostaResponseDTO {
    private UUID id;
    private AlunoResponseDTO aluno;
    private String comentario;
    private Integer likes;

    public RespostaResponseDTO(Resposta resposta) {
        this.id = resposta.getId();
        this.aluno = new AlunoResponseDTO(resposta.getAluno());
        this.comentario = resposta.getComentario();
        this.likes = resposta.getLikes();
    }
}
