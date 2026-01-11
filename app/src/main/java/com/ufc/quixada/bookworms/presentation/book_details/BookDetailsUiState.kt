package com.ufc.quixada.bookworms.presentation.book_details

import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.model.Review
import com.ufc.quixada.bookworms.domain.model.ShelfType

data class BookDetailsUiState(
    val book: Book? = null,
    val isFavorite: Boolean = false,
    val shelfType: ShelfType? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val textoResenha: String = "",
    val contemSpoiler: Boolean = false,
    val nota: Int = 0,
    val reviews: List<Review> = emptyList()
)