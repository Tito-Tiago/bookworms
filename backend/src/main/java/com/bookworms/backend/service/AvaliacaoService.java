package com.bookworms.backend.service;

import com.bookworms.backend.dto.Avaliacao.RespostaCadastroDTO;
import com.bookworms.backend.dto.Avaliacao.RespostaResponseDTO;
import com.bookworms.backend.dto.Avaliacao.AvaliacaoCadastroDTO;
import com.bookworms.backend.dto.Avaliacao.AvaliacaoResponseDTO;
import com.bookworms.backend.model.Aluno;
import com.bookworms.backend.model.Livro;
import com.bookworms.backend.model.Resposta;
import com.bookworms.backend.model.Avaliacao;
import com.bookworms.backend.repository.AlunoRepository;
import com.bookworms.backend.repository.LivroRepository;
import com.bookworms.backend.repository.RespostaRepository;
import com.bookworms.backend.repository.AvaliacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final RespostaRepository respostaRepository;
    private final LivroRepository livroRepository;
    private final AlunoRepository alunoRepository;

    public AvaliacaoResponseDTO createAvaliacao(AvaliacaoCadastroDTO dto) {
        Livro livro = livroRepository.findById(UUID.fromString(dto.getLivroId()))
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado"));
        Aluno aluno = alunoRepository.findById(UUID.fromString(dto.getAlunoId()))
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        Avaliacao avaliacao = new Avaliacao(livro, aluno, dto.getRating(), dto.getComentario());
        
        livro.setSomaAvaliacoes(livro.getSomaAvaliacoes() + dto.getRating());
        livro.setNumAvaliacoes(livro.getNumAvaliacoes() + 1);
        
        avaliacaoRepository.save(avaliacao);
        livroRepository.save(livro);

        return new AvaliacaoResponseDTO(avaliacao);
    }

    public RespostaResponseDTO createResposta(RespostaCadastroDTO dto) {
        Avaliacao avaliacao = avaliacaoRepository.findById(UUID.fromString(dto.getAvaliacaoId()))
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));
        Aluno aluno = alunoRepository.findById(UUID.fromString(dto.getAlunoId()))
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        Resposta resposta = new Resposta(avaliacao, aluno, dto.getComentario());
        respostaRepository.save(resposta);

        return new RespostaResponseDTO(resposta);
    }

    public AvaliacaoResponseDTO likeAvaliacao(UUID id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));
        avaliacao.setLikes(avaliacao.getLikes() + 1);
        avaliacaoRepository.save(avaliacao);
        return new AvaliacaoResponseDTO(avaliacao);
    }

    public RespostaResponseDTO likeResposta(UUID id) {
        Resposta resposta = respostaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada"));
        resposta.setLikes(resposta.getLikes() + 1);
        respostaRepository.save(resposta);
        return new RespostaResponseDTO(resposta);
    }
}
