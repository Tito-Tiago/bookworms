package com.ufc.quixada.bookworms.domain.repository

sealed class FavoriteResult {
    data class Success(val isFavorite: Boolean) : FavoriteResult()
    data class Error(val message: String) : FavoriteResult()
}

sealed class FavoriteListResult {
    data class Success(val bookIds: List<String>) : FavoriteListResult()
    data class Error(val message: String) : FavoriteListResult()
}

interface FavoriteRepository {
    suspend fun isBookFavorite(userId: String, bookId: String): FavoriteResult
    suspend fun addFavorite(userId: String, bookId: String): FavoriteResult
    suspend fun removeFavorite(userId: String, bookId: String): FavoriteResult
    suspend fun getUserFavorites(userId: String): FavoriteListResult
}