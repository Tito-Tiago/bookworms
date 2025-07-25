package com.bookworms.backend.dto.aluno;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private AlunoResponseDTO aluno;
}