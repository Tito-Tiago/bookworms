package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class Shelf(
    val shelfId: String = "",
    val userId: String = "",
    val nomeEstante: String = "",
    val tipo: ShelfType = ShelfType.CUSTOM,
    val dataCriacao: Date = Date()
)

enum class ShelfType {
    LIDO,
    LENDO,
    QUERO_LER,
    CUSTOM
}