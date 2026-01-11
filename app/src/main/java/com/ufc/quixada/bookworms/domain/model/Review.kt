package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class Review(
    val reviewId: String = "",
    val userId: String = "",
    val bookId: String = "",
    val nota: Int = 0, // 1 a 5
    val userName: String = "",
    val textoResenha: String = "",
    val contemSpoiler: Boolean = false,
    val dataCriacao: Date = Date(),
    val dataAtualizacao: Date? = null
)