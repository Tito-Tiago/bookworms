package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class User(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val bio: String? = null,
    val dataCadastro: Date = Date(),
    val role: UserRole = UserRole.ALUNO,
    val fcmToken: String? = null
)