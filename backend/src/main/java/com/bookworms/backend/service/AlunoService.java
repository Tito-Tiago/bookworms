package com.bookworms.backend.service;

import com.bookworms.backend.dto.aluno.AlunoCadastroDTO;
import com.bookworms.backend.dto.aluno.AlunoResponseDTO;
import com.bookworms.backend.model.Aluno;
import com.bookworms.backend.repository.AlunoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private static final String FOTO_PERFIL_PADRAO_URL = "https://pin.it/Cg3X3D8YE";
    public AlunoResponseDTO cadastrarAluno(AlunoCadastroDTO dto) {
        alunoRepository.findByEmailOrUsername(dto.getEmail(), dto.getUsername())
                .ifPresent(aluno -> {
                    throw new IllegalArgumentException("E-mail ou nome de usuário já cadastrado.");
                });

        Aluno novoAluno = new Aluno(
                dto.getNomeCompleto(),
                dto.getEmail(),
                dto.getUsername(),
                dto.getSenha()
        );

        novoAluno.setFotoPerfilUrl(FOTO_PERFIL_PADRAO_URL);

        Aluno alunoSalvo = alunoRepository.save(novoAluno);

        return new AlunoResponseDTO(alunoSalvo);
    }

    public Aluno buscarAluno(UUID id) {
        return alunoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));
    }
}