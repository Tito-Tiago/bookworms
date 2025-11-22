package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class ShelfItem(
    val shelfItemId: String = "",
    val shelfId: String = "",
    val bookId: String = "",
    val progressoPaginas: Int? = null,
    val dataAdicao: Date = Date()
)