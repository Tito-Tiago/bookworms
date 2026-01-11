package com.ufc.quixada.bookworms.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.ufc.quixada.bookworms.domain.model.Shelf
import com.ufc.quixada.bookworms.domain.model.ShelfItem
import com.ufc.quixada.bookworms.domain.model.ShelfType
import com.ufc.quixada.bookworms.domain.repository.ShelfRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShelfRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ShelfRepository {

    override suspend fun addBookToShelf(userId: String, bookId: String, shelfType: ShelfType): Result<Unit> {
        return try {
            val standardTypes = listOf(ShelfType.LIDO, ShelfType.LENDO, ShelfType.QUERO_LER)

            // 1. Busca as estantes padrão do usuário
            val shelvesSnapshot = firestore.collection("shelves")
                .whereEqualTo("userId", userId)
                .whereIn("tipo", standardTypes.map { it.name })
                .get().await()

            val shelfMap = shelvesSnapshot.documents.associateBy { it.getString("tipo") }
            val allStandardShelfIds = shelvesSnapshot.map { it.id }

            // 2. Garante que a estante de destino existe
            val targetShelfId = shelfMap[shelfType.name]?.id ?: run {
                val shelfRef = firestore.collection("shelves").document()
                val newShelf = Shelf(
                    shelfId = shelfRef.id,
                    userId = userId,
                    nomeEstante = shelfType.name,
                    tipo = shelfType
                )
                shelfRef.set(newShelf).await()
                shelfRef.id
            }

            // 3. Remove o livro de qualquer estante padrão onde ele já esteja
            // CORREÇÃO: Verifica se a lista não está vazia antes de usar whereIn
            if (allStandardShelfIds.isNotEmpty()) {
                val existingItems = firestore.collection("shelf_items")
                    .whereEqualTo("bookId", bookId)
                    .whereIn("shelfId", allStandardShelfIds)
                    .get().await()

                for (doc in existingItems) {
                    doc.reference.delete().await()
                }
            }

            // 4. Adiciona à nova estante
            val itemRef = firestore.collection("shelf_items").document()
            val newItem = ShelfItem(
                shelfItemId = itemRef.id,
                shelfId = targetShelfId,
                bookId = bookId
            )
            itemRef.set(newItem).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ShelfRepository", "Erro detalhado: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getBookShelfStatus(userId: String, bookId: String): ShelfType? {
        return try {
            val standardTypes = listOf(ShelfType.LIDO, ShelfType.LENDO, ShelfType.QUERO_LER)

            val shelvesSnapshot = firestore.collection("shelves")
                .whereEqualTo("userId", userId)
                .whereIn("tipo", standardTypes.map { it.name })
                .get().await()

            val shelfIds = shelvesSnapshot.map { it.id }
            if (shelfIds.isEmpty()) return null

            val itemSnapshot = firestore.collection("shelf_items")
                .whereEqualTo("bookId", bookId)
                .whereIn("shelfId", shelfIds)
                .get().await()

            val item = itemSnapshot.documents.firstOrNull() ?: return null
            val shelfId = item.getString("shelfId")

            val shelfDoc = shelvesSnapshot.documents.find { it.id == shelfId }
            shelfDoc?.getString("tipo")?.let { ShelfType.valueOf(it) }
        } catch (e: Exception) {
            Log.e("ShelfRepository", "Erro ao buscar status: ${e.message}")
            null
        }
    }
}