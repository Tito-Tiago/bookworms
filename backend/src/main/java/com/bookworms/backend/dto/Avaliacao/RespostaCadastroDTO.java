package com.bookworms.backend.dto.Avaliacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RespostaCadastroDTO {

    @NotNull(message = "O ID da avaliação é obrigatório")
    private String avaliacaoId;

    @NotNull(message = "O ID do aluno é obrigatório")
    private String alunoId;

    @NotBlank(message = "O comentário é obrigatório")
    private String comentario;
}
