package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class Follow(
    val followId: String = "",
    val userIdSeguidor: String = "", // Quem segue
    val userIdSeguido: String = "", // Quem é seguido
    val dataInicio: Date = Date()
)