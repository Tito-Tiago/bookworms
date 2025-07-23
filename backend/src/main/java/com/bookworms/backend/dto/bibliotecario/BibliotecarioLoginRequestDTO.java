package com.bookworms.backend.dto.bibliotecario;

import lombok.Data;

@Data
public class BibliotecarioLoginRequestDTO {
    private String username;
    private String password;
}