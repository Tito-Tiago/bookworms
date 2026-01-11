package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.ShelfType

interface ShelfRepository {
    suspend fun addBookToShelf(userId: String, bookId: String, shelfType: ShelfType): Result<Unit>
    suspend fun getBookShelfStatus(userId: String, bookId: String): ShelfType?
}