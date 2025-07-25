package com.bookworms.backend.dto.livro;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LivroCadastroDTO {

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "A sinopse é obrigatória")
    private String sinopse;

    @NotBlank(message = "O gênero é obrigatório")
    private String genero;

    @NotBlank(message = "O autor é obrigatório")
    private String autor;
}
