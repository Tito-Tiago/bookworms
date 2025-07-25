package com.bookworms.backend.repository;

import com.bookworms.backend.model.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, UUID> {
}
