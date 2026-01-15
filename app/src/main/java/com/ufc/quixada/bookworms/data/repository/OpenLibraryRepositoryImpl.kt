package com.ufc.quixada.bookworms.data.repository

import com.ufc.quixada.bookworms.data.remote.OpenLibraryBookDetailsDto
import com.ufc.quixada.bookworms.data.remote.OpenLibraryRatingResponse
import com.ufc.quixada.bookworms.data.remote.OpenLibrarySearchResponse
import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.repository.BookResult
import com.ufc.quixada.bookworms.domain.repository.OpenLibraryRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class OpenLibraryRepositoryImpl @Inject constructor(
    private val client: HttpClient
): OpenLibraryRepository {
    private val apiBase = "https://openlibrary.org/search.json"
    private val detailsBase = "https://openlibrary.org"

    override suspend fun searchBooks(query: String): BookResult {
        return try {
            val response: OpenLibrarySearchResponse = client.get(apiBase) {
                parameter("q", query)
                parameter("limit", 20)
            }.body()

            val books = response.docs.mapNotNull { doc ->
                if (doc.key == null || doc.title == null) return@mapNotNull null

                Book(
                    bookId = doc.key.substringAfterLast("/"),
                    titulo = doc.title,
                    autor = doc.authorName?.joinToString(", ") ?: "Autor Desconhecido",
                    capaUrl = getCoverUrl(doc.coverId),
                    fonteApi = "OpenLibrary",
                    isbn = doc.isbn?.firstOrNull(),
                    numAvaliacoes = 0,
                    sinopse = ""
                )
            }

            BookResult.Success(books)
        } catch (e: Exception) {
            e.printStackTrace()
            BookResult.Error("Erro ao buscar livros: ${e.message}")
        }
    }

    override suspend fun getBookDetails(bookId: String): Result<Book> = coroutineScope {
        try {
            val cleanId = bookId.substringAfterLast("/")
            val isWork = cleanId.endsWith("W")
            val endpoint = if (isWork) "works" else "books"

            val ratingsDeferred = async {
                try {
                    client.get("$detailsBase/works/$cleanId/ratings.json").body<OpenLibraryRatingResponse>()
                } catch (e: Exception) { null }
            }

            val detailsDeferred = async {
                try {
                    client.get("$detailsBase/$endpoint/$cleanId.json").body<OpenLibraryBookDetailsDto>()
                } catch (e: Exception) { null }
            }

            val ratings = ratingsDeferred.await()
            val details = detailsDeferred.await()

            val descriptionText = details?.description?.let { jsonElement ->
                if (jsonElement is kotlinx.serialization.json.JsonObject) {
                    jsonElement["value"]?.toString()?.removeSurrounding("\"")?.replace("\\n", "\n")
                } else {
                    jsonElement.toString().removeSurrounding("\"").replace("\\n", "\n")
                }
            } ?: "Sem sinopse disponível."

            val book = Book(
                bookId = bookId,
                sinopse = descriptionText,
                notaApiExterna = ratings?.summary?.average ?: 0f,
                isbn = details?.isbn13?.firstOrNull() ?: details?.isbn10?.firstOrNull()
            )
            Result.success(book)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getCoverUrl(coverId: Int?): String? {
        return coverId?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" }
    }
}