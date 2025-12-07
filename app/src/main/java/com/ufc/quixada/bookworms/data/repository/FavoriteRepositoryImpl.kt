package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ufc.quixada.bookworms.domain.model.Favorite
import com.ufc.quixada.bookworms.domain.repository.FavoriteListResult
import com.ufc.quixada.bookworms.domain.repository.FavoriteRepository
import com.ufc.quixada.bookworms.domain.repository.FavoriteResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FavoriteRepository {

    override suspend fun isBookFavorite(userId: String, bookId: String): FavoriteResult {
        return try {
            val query = firestore.collection("favorites")
                .whereEqualTo("userId", userId)
                .whereEqualTo("bookId", bookId)
                .get()
                .await()

            FavoriteResult.Success(!query.isEmpty)
        } catch (e: Exception) {
            FavoriteResult.Error(e.message ?: "Erro ao verificar favorito")
        }
    }

    override suspend fun addFavorite(userId: String, bookId: String): FavoriteResult {
        return try {
            val docId = "${userId}_${bookId}"
            val favorite = Favorite(
                favoriteId = UUID.randomUUID().toString(),
                userId = userId,
                bookId = bookId,
                dataFavoritado = Date()
            )

            firestore.collection("favorites")
                .document(docId)
                .set(favorite)
                .await()

            FavoriteResult.Success(true)
        } catch (e: Exception) {
            FavoriteResult.Error(e.message ?: "Erro ao adicionar favorito")
        }
    }

    override suspend fun removeFavorite(userId: String, bookId: String): FavoriteResult {
        return try {
            val docId = "${userId}_${bookId}"

            firestore.collection("favorites")
                .document(docId)
                .delete()
                .await()

            FavoriteResult.Success(false)
        } catch (e: Exception) {
            FavoriteResult.Error(e.message ?: "Erro ao remover favorito")
        }
    }

    override suspend fun getUserFavorites(userId: String): FavoriteListResult {
        return try {
            val snapshot = firestore.collection("favorites")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val bookIds = snapshot.documents.mapNotNull { doc ->
                doc.getString("bookId")
            }

            FavoriteListResult.Success(bookIds)
        } catch (e: Exception) {
            FavoriteListResult.Error(e.message ?: "Erro ao buscar favoritos")
        }
    }

    // Implementação do fluxo em tempo real
    override fun getFavoritesFlow(userId: String): Flow<List<String>> = callbackFlow {
        val listener = firestore.collection("favorites")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val bookIds = snapshot?.documents?.mapNotNull { doc ->
                    doc.getString("bookId")
                } ?: emptyList()

                trySend(bookIds)
            }

        awaitClose { listener.remove() }
    }
}