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
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class OpenLibraryRepositoryImpl @Inject constructor(
    private val client: HttpClient
): OpenLibraryRepository {
    private val apiBase = "https://openlibrary.org/search.json"
    private val detailsBase = "https://openlibrary.org" // /books/ ou /works/

    override suspend fun searchBooks(query: String): BookResult {
        return try {
            val response: OpenLibrarySearchResponse = client.get(apiBase) {
                parameter("q", query)
                parameter("fields", "key,title,author_name,cover_i,editions,editions.title,editions.language,editions.cover_i,editions.key,editions.isbn")
                parameter("limit", 12)
            }.body()

            val books = response.docs.mapNotNull { doc ->
                val ptEditions = doc.editions?.docs?.filter {
                    it.language?.any { lang -> lang.contains("por", ignoreCase = true) } == true
                } ?: emptyList()

                val selectedEdition = if (ptEditions.isNotEmpty()) {
                    ptEditions.maxByOrNull { it.coverId != null } ?: ptEditions.first()
                } else {
                    doc.editions?.docs?.maxByOrNull { it.coverId != null } ?: doc.editions?.docs?.firstOrNull()
                }

                val finalTitle: String
                val finalCoverId: Int?
                val finalKey: String
                val finalIsbn: String?

                if (selectedEdition != null) {
                    finalTitle = selectedEdition.title
                    finalCoverId = selectedEdition.coverId
                    finalKey = selectedEdition.key
                    finalIsbn = selectedEdition.isbn?.firstOrNull()
                } else {
                    finalTitle = doc.title
                    finalCoverId = doc.coverId
                    finalKey = doc.key
                    finalIsbn = null
                }

                Book(
                    bookId = finalKey.substringAfterLast("/"),
                    titulo = finalTitle,
                    autor = doc.authorName?.joinToString(", ") ?: "Autor Desconhecido",
                    capaUrl = getCoverUrl(finalCoverId),
                    fonteApi = "OpenLibrary",
                    isbn = finalIsbn
                )
            }
            BookResult.Success(books)
        } catch (e: Exception) {
            e.printStackTrace()
            BookResult.Error(e.message ?: "Erro interno")
        }
    }

    override suspend fun getBookDetails(bookId: String): Result<Book> = coroutineScope {
        try {
            val isWork = bookId.endsWith("W")
            val endpoint = if (isWork) "works" else "books"

            // Busca detalhes 
            val details = try {
                client.get("$detailsBase/$endpoint/$bookId.json").body<OpenLibraryBookDetailsDto>()
            } catch (e: Exception) { null }

            val workId = if (isWork) {
                bookId
            } else {
                details?.works?.firstOrNull()?.key?.substringAfterLast("/")
            }

            //busca avaliações
            val ratings = if (workId != null) {
                try {
                    client.get("$detailsBase/works/$workId/ratings.json").body<OpenLibraryRatingResponse>()
                } catch (e: Exception) { null }
            } else null

            //parse da descrição
            val descriptionText = details?.description?.let { jsonElement ->
                if (jsonElement is kotlinx.serialization.json.JsonObject) {
                    jsonElement["value"]?.toString()?.removeSurrounding("\"")
                } else {
                    jsonElement.toString().removeSurrounding("\"")
                }
            } ?: ""

            val book = Book(
                bookId = bookId,
                sinopse = descriptionText,
                notaApiExterna = ratings?.summary?.average ?: 0f,
                isbn = details?.isbn13?.joinToString(", ") ?: details?.isbn10?.joinToString(", ")
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