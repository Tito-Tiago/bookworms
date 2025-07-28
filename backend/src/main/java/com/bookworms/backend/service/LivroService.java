package com.bookworms.backend.service;

import com.bookworms.backend.dto.livro.LivroCadastroDTO;
import com.bookworms.backend.dto.livro.LivroResponseDTO;
import com.bookworms.backend.model.Livro;
import com.bookworms.backend.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LivroService {
    private static final String LIVRO_URL = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.dc5.ro%2Fimg-prod%2F1728045377-5.jpeg&f=1&nofb=1&ipt=c250928c058af85391c169f0bf5b7f407fc678e3cf9adea25694fb1282bb4aa6";

    @Autowired
    private LivroRepository livroRepository;

    public LivroResponseDTO getLivroById(UUID id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        return new LivroResponseDTO(livro);
    }

    public LivroResponseDTO cadastrarLivro(LivroCadastroDTO dto) {
        if (livroRepository.existsByTitulo(dto.getTitulo())) {
            throw new IllegalArgumentException("Titulo cadastrado");
        }

        Livro novoLivro = new Livro(
                dto.getTitulo(),
                dto.getSinopse(),
                dto.getGenero(),
                dto.getAutor()
        );

        novoLivro.setFotoCapaUrl(LIVRO_URL);

        Livro livroSalvo = livroRepository.save(novoLivro);
        return new LivroResponseDTO(livroSalvo);
    }

    public List<LivroResponseDTO> getLivros() {
        return livroRepository.findAll().stream()
                .map(LivroResponseDTO::new)
                .collect(Collectors.toList());
    }

    public void deletarLivro(UUID id){
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));

        livroRepository.delete(livro);
    }

    public LivroResponseDTO like(UUID id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        livro.setLikes(livro.getLikes() + 1);
        livroRepository.save(livro);
        return new LivroResponseDTO(livro);
    }

    public LivroResponseDTO unlike(UUID id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        livro.setLikes(Math.max(0, livro.getLikes() - 1));
        livroRepository.save(livro);
        return new LivroResponseDTO(livro);
    }

    

}
