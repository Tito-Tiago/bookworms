package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.BookResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : BookRepository {

    override suspend fun getBooks(): BookResult {
        return try {
            val snapshot = firestore.collection("books")
                .limit(20) // Limitando para o catálogo inicial
                .get()
                .await()

            val books = snapshot.toObjects(Book::class.java)
            BookResult.Success(books)
        } catch (e: Exception) {
            BookResult.Error(e.message ?: "Erro ao buscar livros")
        }
    }
}