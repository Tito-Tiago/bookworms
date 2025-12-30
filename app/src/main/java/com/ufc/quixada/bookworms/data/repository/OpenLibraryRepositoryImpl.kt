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

                val finalTitle: String
                val finalCoverId: Int?
                val finalKey: String
                val finalIsbn: String?

                if (ptEditions.isNotEmpty()) {
                    val bestEdition = ptEditions.maxByOrNull { it.coverId != null }!!
                    finalTitle = bestEdition.title
                    finalCoverId = bestEdition.coverId
                    finalKey = bestEdition.key
                    finalIsbn = bestEdition.isbn?.firstOrNull()
                } else {
                    finalTitle = doc.title
                    finalCoverId = doc.converId
                    finalKey = doc.key
                    val bestAnyEdition = doc.editions?.docs?.maxByOrNull { it.coverId != null }
                    finalIsbn = bestAnyEdition?.isbn?.firstOrNull()
                }

                Book(
                    bookId = finalKey,
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

    override suspend fun getBookDetails(
        workKey: String,
        editionKey: String?
    ): Result<Book> = coroutineScope {
        try {
            val rattingsDeferred = async {
                try {
                    // /works/OL123W -> OL123W
                    val workId = workKey.substringAfterLast("/")
                    client.get("$detailsBase/works/$workId/ratings.json").body<OpenLibraryRatingResponse>()
                } catch (e: Exception) { null }
            }

            val detailsDeferred = async {
                if (editionKey != null) {
                    try {
                        val editionId = editionKey.substringAfterLast("/")
                        client.get("$detailsBase/books/$editionId.json").body<OpenLibraryBookDetailsDto>()
                    } catch (e: Exception) { null }
                } else null
            }

            val ratings = rattingsDeferred.await()
            val details = detailsDeferred.await()
            
            val descriptionText = details?.description?.let { jsonElement ->
                if (jsonElement is kotlinx.serialization.json.JsonObject) {
                    jsonElement["value"]?.toString()?.removeSurrounding("\"")
                } else {
                    jsonElement.toString().removeSurrounding("\"")
                }
            } ?: "Sinopse indisponível."

            val book = Book(
                bookId = editionKey ?: workKey,
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