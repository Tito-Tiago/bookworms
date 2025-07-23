package com.bookworms.backend.repository;

import com.bookworms.backend.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LivroRepository extends JpaRepository<Livro, UUID> {
    Livro findLivroById(UUID id);
    Boolean existsByTitulo(String titulo);
}
