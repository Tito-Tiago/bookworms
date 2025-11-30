package com.ufc.quixada.bookworms.presentation.book_details

import com.ufc.quixada.bookworms.domain.model.Book

data class BookDetailsUiState(
    val book: Book? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)