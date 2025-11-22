package com.ufc.quixada.bookworms.domain.model

import java.util.Date

data class Favorite(
    val favoriteId: String = "",
    val userId: String = "",
    val bookId: String = "",
    val dataFavoritado: Date = Date()
)