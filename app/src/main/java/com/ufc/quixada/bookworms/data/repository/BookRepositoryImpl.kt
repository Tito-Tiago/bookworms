package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.BookResult
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : BookRepository {

    override suspend fun getBooks(): BookResult {
        return try {
            val snapshot = firestore.collection("books")
                .limit(20)
                .get()
                .await()

            val books = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Book::class.java)?.copy(bookId = doc.id)
            }

            BookResult.Success(books)
        } catch (e: Exception) {
            BookResult.Error(e.message ?: "Erro ao buscar livros")
        }
    }

    override suspend fun getBook(bookId: String): SingleBookResult {
        return try {
            val document = firestore.collection("books")
                .document(bookId)
                .get()
                .await()

            val book = document.toObject(Book::class.java)

            if (book != null) {
                val bookWithId = book.copy(bookId = document.id)
                SingleBookResult.Success(bookWithId)
            } else {
                SingleBookResult.Error("Livro não encontrado")
            }
        } catch (e: Exception) {
            SingleBookResult.Error(e.message ?: "Erro ao buscar detalhes do livro")
        }
    }

    override suspend fun searchBooks(query: String): BookResult {
        return try {
            val snapshot = firestore.collection("books")
                .whereGreaterThanOrEqualTo("titulo", query)
                .whereLessThanOrEqualTo("titulo", query + "\uf8ff")
                .get()
                .await()

            val books = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Book::class.java)?.copy(bookId = doc.id)
            }

            BookResult.Success(books)
        } catch (e: Exception) {
            BookResult.Error(e.message ?: "Erro ao buscar livros")
        }
    }
}