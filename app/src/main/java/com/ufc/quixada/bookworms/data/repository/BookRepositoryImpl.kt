package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ufc.quixada.bookworms.data.local.dao.BookDao
import com.ufc.quixada.bookworms.data.local.entity.BookEntity
import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.BookResult
import com.ufc.quixada.bookworms.domain.repository.SimpleBookResult
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val bookDao: BookDao
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
            //tenta achar no room
            val localBook = bookDao.getBook(bookId)
            if (localBook != null) {
                return SingleBookResult.Success(mapEntityToDomain(localBook))
            }

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

    override suspend fun saveBook(book: Book): SimpleBookResult {
        try {
            firestore.collection("books")
                .document(book.bookId)
                .set(book)
                .await()
        } catch (e: Exception) {
            return SimpleBookResult.Error(e.message ?: "Erro ao salvar no firebase")
        }

        val entity = BookEntity(
            bookId = book.bookId,
            titulo = book.titulo,
            autor = book.autor,
            sinopse = book.sinopse,
            capaUrl = book.capaUrl,
            isbn = book.isbn,
            notaMediaComunidade = book.notaMediaComunidade,
            notaApiExterna = book.notaApiExterna,
            fonteApi = book.fonteApi,
            numAvaliacoes = book.numAvaliacoes
        )
        bookDao.insertBook(entity)

        return SimpleBookResult.Success
    }

    override suspend fun getTrendingBooks(): BookResult {
        return try {
            // Ordena por número de avaliações (mais populares)
            val snapshot = firestore.collection("books")
                .orderBy("numAvaliacoes", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            val books = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Book::class.java)?.copy(bookId = doc.id)
            }
            BookResult.Success(books)
        } catch (e: Exception) {
            BookResult.Error(e.message ?: "Erro ao buscar livros em alta")
        }
    }

    override suspend fun getRecentBooks(): BookResult {
        return try {
            // Ordena por data de cadastro (novidades)
            val snapshot = firestore.collection("books")
                .orderBy("dataCadastro", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            val books = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Book::class.java)?.copy(bookId = doc.id)
            }
            BookResult.Success(books)
        } catch (e: Exception) {
            BookResult.Error(e.message ?: "Erro ao buscar novidades")
        }
    }

    private fun mapEntityToDomain(entity: BookEntity) = Book(
        bookId = entity.bookId,
        titulo = entity.titulo,
        autor = entity.autor,
        sinopse = entity.sinopse,
        capaUrl = entity.capaUrl,
        isbn = entity.isbn,
        notaMediaComunidade = entity.notaMediaComunidade,
        notaApiExterna = entity.notaApiExterna,
        fonteApi = entity.fonteApi,
        numAvaliacoes = entity.numAvaliacoes
    )
}