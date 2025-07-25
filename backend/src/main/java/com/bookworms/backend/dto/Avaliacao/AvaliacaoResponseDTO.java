package com.bookworms.backend.dto.Avaliacao;

import com.bookworms.backend.dto.aluno.AlunoResponseDTO;
import com.bookworms.backend.model.Avaliacao;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class AvaliacaoResponseDTO {
    private UUID id;
    private AlunoResponseDTO aluno;
    private Integer numEstrelas;
    private String comentario;
    private Integer likes;
    private List<RespostaResponseDTO> replies;

    public AvaliacaoResponseDTO(Avaliacao avaliacao) {
        this.id = avaliacao.getId();
        this.aluno = new AlunoResponseDTO(avaliacao.getAluno());
        this.numEstrelas = avaliacao.getNumEstrelas();
        this.comentario = avaliacao.getComentario();
        this.likes = avaliacao.getLikes();
        this.replies = avaliacao.getRespostas().stream().map(RespostaResponseDTO::new).collect(Collectors.toList());
    }
}
