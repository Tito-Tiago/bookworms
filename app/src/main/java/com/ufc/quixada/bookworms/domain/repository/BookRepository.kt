package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.Book

sealed class BookResult {
    data class Success(val data: List<Book>) : BookResult()
    data class Error(val message: String) : BookResult()
}

sealed class SingleBookResult {
    data class Success(val data: Book) : SingleBookResult()
    data class Error(val message: String) : SingleBookResult()
}

sealed class SimpleBookResult {
    object Success : SimpleBookResult()
    data class Error(val message: String) : SimpleBookResult()
}

interface BookRepository {
    suspend fun getBooks(): BookResult
    suspend fun getBook(bookId: String): SingleBookResult
    suspend fun searchBooks(query: String): BookResult
    suspend fun saveBook(book: Book) : SimpleBookResult
    suspend fun getTrendingBooks(): BookResult
    suspend fun getRecentBooks(): BookResult
}