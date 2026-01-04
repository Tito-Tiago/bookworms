package com.ufc.quixada.bookworms.domain.usecase

import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.BookResult
import com.ufc.quixada.bookworms.domain.repository.OpenLibraryRepository
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val openLibraryRepository: OpenLibraryRepository
) {
    suspend operator fun invoke(query: String): BookResult {
        if (query.isBlank()) {
            return bookRepository.getBooks()
        }
        
        val localResult = bookRepository.searchBooks(query)
        val localBooks = if (localResult is BookResult.Success) localResult.data else emptyList()

        val apiResult = openLibraryRepository.searchBooks(query)
        val apiBooks = if (apiResult is BookResult.Success) apiResult.data else emptyList()

        val allBooks = (localBooks + apiBooks).distinctBy { it.bookId }

        return if (allBooks.isNotEmpty()) {
            BookResult.Success(allBooks)
        } else {
            apiResult as? BookResult.Error ?: BookResult.Error("Nenhum livro encontrado.")
        }
    }
}