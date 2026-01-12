package com.ufc.quixada.bookworms.presentation.home

import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.model.User
enum class SearchType {
    BOOKS, READERS
}

data class HomeUiState(
    val searchQuery: String = "",
    val searchType: SearchType = SearchType.BOOKS,
    val books: List<Book> = emptyList(),
    val readers: List<User> = emptyList(),
    val favoriteBookIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)