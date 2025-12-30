package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.Book

interface OpenLibraryRepository {
    suspend fun searchBooks(query: String): Result<List<Book>>
    suspend fun getBookDetails(workKey: String, editionKey: String?): Result<Book>
}