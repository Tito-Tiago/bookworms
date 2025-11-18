package com.ufc.quixada.bookworms.domain.model

data class User(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val fotoUrl: String? = null,
    val turma: String? = null,
    val role: String = "aluno" // aluno ou docente
)