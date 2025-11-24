package com.ufc.quixada.bookworms.presentation.home

import com.ufc.quixada.bookworms.domain.model.Book

data class HomeUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)