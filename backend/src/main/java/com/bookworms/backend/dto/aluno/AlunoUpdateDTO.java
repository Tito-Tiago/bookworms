package com.bookworms.backend.dto.aluno;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlunoUpdateDTO {

    @Size(min = 3, message = "O nome completo deve ter pelo menos 3 caracteres.")
    private String nomeCompleto;

    @Email(message = "Formato de e-mail inválido.")
    private String email;

    @Size(min = 3, message = "O nome de usuário deve ter pelo menos 3 caracteres.")
    private String username;

    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    private String senha;
}