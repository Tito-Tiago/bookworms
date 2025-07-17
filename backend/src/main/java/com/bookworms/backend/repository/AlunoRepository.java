package com.bookworms.backend.repository;

import com.bookworms.backend.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, UUID> {
    Optional<Aluno> findByEmailOrUsername(String email, String username);
}