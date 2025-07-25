package com.bookworms.backend.service;

import com.bookworms.backend.dto.aluno.AlunoCadastroDTO;
import com.bookworms.backend.dto.aluno.AlunoResponseDTO;
import com.bookworms.backend.dto.aluno.AlunoUpdateDTO;
import com.bookworms.backend.dto.aluno.LoginRequestDTO;
import com.bookworms.backend.dto.aluno.LoginResponseDTO;
import com.bookworms.backend.model.Aluno;
import com.bookworms.backend.repository.AlunoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private static final String FOTO_PERFIL_PADRAO_URL = "https://images.unsplash.com/photo-1695457601176-b779cf426428?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

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

    public LoginResponseDTO login(LoginRequestDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        Aluno alunoAutenticado = alunoRepository.findByEmailOrUsername(auth.getName(), auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("Erro ao recuperar dados do aluno após login."));

        var token = tokenService.generateToken(alunoAutenticado);
        var alunoResponse = new AlunoResponseDTO(alunoAutenticado);

        return new LoginResponseDTO(token, alunoResponse);
    }

    public AlunoResponseDTO atualizarAluno(UUID id, AlunoUpdateDTO dto) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));

        if (dto.getNomeCompleto() != null && !dto.getNomeCompleto().isBlank()) {
            aluno.setNomeCompleto(dto.getNomeCompleto());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            aluno.setEmail(dto.getEmail());
        }

        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            aluno.setUsername(dto.getUsername());
        }
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            aluno.setSenha(dto.getSenha());
        }

        Aluno alunoAtualizado = alunoRepository.save(aluno);

        return new AlunoResponseDTO(alunoAtualizado);
    }

    public AlunoResponseDTO buscarAlunoPorId(UUID id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado."));
        return new AlunoResponseDTO(aluno);
    }

    public void excluirAluno(UUID id) {
        if (!alunoRepository.existsById(id)) {
            throw new EntityNotFoundException("Aluno não encontrado para exclusão.");
        }

        alunoRepository.deleteById(id);
    }
}