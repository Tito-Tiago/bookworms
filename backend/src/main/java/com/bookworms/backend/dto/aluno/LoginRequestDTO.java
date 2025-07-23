package com.bookworms.backend.dto.aluno;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "O e-mail ou nome de usuário é obrigatório.")
    private String login;

    @NotBlank(message = "A senha é obrigatória.")
    private String senha;
}