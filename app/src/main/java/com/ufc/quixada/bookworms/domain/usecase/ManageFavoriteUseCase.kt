package com.ufc.quixada.bookworms.domain.usecase

import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.FavoriteListResult
import com.ufc.quixada.bookworms.domain.repository.FavoriteRepository
import com.ufc.quixada.bookworms.domain.repository.FavoriteResult
import javax.inject.Inject

class ManageFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val authRepository: AuthRepository
) {
    suspend fun checkFavoriteStatus(bookId: String): FavoriteResult {
        val currentUser = authRepository.getCurrentUser()
            ?: return FavoriteResult.Error("Usuário não logado")

        return favoriteRepository.isBookFavorite(currentUser.uid, bookId)
    }

    suspend fun toggleFavorite(bookId: String, isCurrentlyFavorite: Boolean): FavoriteResult {
        val currentUser = authRepository.getCurrentUser()
            ?: return FavoriteResult.Error("Usuário não logado")

        return if (isCurrentlyFavorite) {
            favoriteRepository.removeFavorite(currentUser.uid, bookId)
        } else {
            favoriteRepository.addFavorite(currentUser.uid, bookId)
        }
    }

    suspend fun getAllFavoriteBookIds(): FavoriteListResult {
        val currentUser = authRepository.getCurrentUser()
            ?: return FavoriteListResult.Error("Usuário não logado")

        return favoriteRepository.getUserFavorites(currentUser.uid)
    }
}