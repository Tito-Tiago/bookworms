package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class User(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val turma: String? = null,
    val role: UserRole = UserRole.ALUNO,
    val dataCadastro: Date = Date()
)

enum class UserRole {
    ALUNO,
    DOCENTE
}