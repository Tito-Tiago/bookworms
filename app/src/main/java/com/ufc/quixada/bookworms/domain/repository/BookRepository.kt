package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.Book
import kotlinx.coroutines.flow.Flow

sealed class BookResult {
    data class Success(val data: List<Book>) : BookResult()
    data class Error(val message: String) : BookResult()
}

interface BookRepository {
    suspend fun getBooks(): BookResult
    // Futuramente adicionaremos searchBooks(query) aqui para o HU-04
}