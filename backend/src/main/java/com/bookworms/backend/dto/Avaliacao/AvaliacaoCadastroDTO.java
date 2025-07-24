package com.bookworms.backend.dto.Avaliacao;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvaliacaoCadastroDTO {

    @NotNull(message = "O ID do livro é obrigatório")
    private String livroId;

    @NotNull(message = "O ID do aluno é obrigatório")
    private String alunoId;

    @NotNull(message = "A avaliação é obrigatória")
    @Min(value = 1, message = "A avaliação deve ser no mínimo 1")
    @Max(value = 5, message = "A avaliação deve ser no máximo 5")
    private Integer rating;

    private String comentario;
}
