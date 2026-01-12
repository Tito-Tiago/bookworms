package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class Follow(
    val followId: String = "",
    val userIdSeguidor: String = "",
    val userIdSeguido: String = "",
    val dataInicio: Date = Date()
)