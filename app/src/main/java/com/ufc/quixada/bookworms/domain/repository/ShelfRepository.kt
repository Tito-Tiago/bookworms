package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.model.ShelfType

data class ShelfWithBooks(
    val shelfType: ShelfType,
    val books: List<Book>
)

interface ShelfRepository {
    suspend fun addBookToShelf(userId: String, bookId: String, shelfType: ShelfType): Result<Unit>
    suspend fun getBookShelfStatus(userId: String, bookId: String): ShelfType?
    suspend fun getUserShelvesWithBooks(userId: String): Result<List<ShelfWithBooks>>
}
