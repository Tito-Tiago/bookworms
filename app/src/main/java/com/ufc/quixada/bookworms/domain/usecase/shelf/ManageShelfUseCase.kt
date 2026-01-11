package com.ufc.quixada.bookworms.domain.usecase.shelf

import com.ufc.quixada.bookworms.domain.model.ShelfType
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.ShelfRepository
import javax.inject.Inject

class ManageShelfUseCase @Inject constructor(
    private val shelfRepository: ShelfRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(bookId: String, shelfType: ShelfType): Result<Unit> {
        val user = authRepository.getCurrentUser() ?: return Result.failure(Exception("Usuário não logado"))
        return shelfRepository.addBookToShelf(user.uid, bookId, shelfType)
    }

    suspend fun getStatus(bookId: String): ShelfType? {
        val user = authRepository.getCurrentUser() ?: return null
        return shelfRepository.getBookShelfStatus(user.uid, bookId)
    }
}