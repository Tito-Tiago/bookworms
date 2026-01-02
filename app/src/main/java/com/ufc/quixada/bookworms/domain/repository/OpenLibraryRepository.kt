package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.Book

interface OpenLibraryRepository {
    suspend fun searchBooks(query: String): BookResult
    suspend fun getBookDetails(bookId: String): Result<Book>
}