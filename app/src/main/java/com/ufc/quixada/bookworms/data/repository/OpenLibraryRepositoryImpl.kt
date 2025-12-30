package com.ufc.quixada.bookworms.data.repository

import com.ufc.quixada.bookworms.data.remote.OpenLibrarySearchResponse
import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.repository.OpenLibraryRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class OpenLibraryRepositoryImpl @Inject constructor(
    private val client: HttpClient
): OpenLibraryRepository {
    private val apiBase = "https://openlibrary.org/search.json"
    private val detailsBase = "https://openlibrary.org" // /books/ or /works/

    override suspend fun searchBooks(query: String): Result<List<Book>> {
        return try {
            val response: OpenLibrarySearchResponse = client.get(apiBase) {
                parameter("q", "title:($query) AND language:por")
                parameter("fields", "key,title,author_name,cover_i,editions,editions.title,editions.language,editions.cover_i,editions.key,editions.isbn")
                parameter("limit", 12)
            }.body()

            val books = response.docs.mapNotNull { doc ->
                val ptEditions = doc.editions?.docs?.filter {
                    it.language?.any { lang -> lang.contains("por") } == true
                } ?: emptyList()

                if (ptEditions.isEmpty()) return@mapNotNull null

                val bestEdition = ptEditions.maxByOrNull { it.coverId != null }!!
                Book(
                    bookId = bestEdition.key,
                    titulo = bestEdition.title,
                    autor = doc.authorName?.joinToString(", ") ?: "Autor Desconhecido",
                    capaUrl = getCoverUrl(bestEdition.coverId ?: doc.converId),
                    fonteApi = "OpenLibrary",
                    isbn = bestEdition.isbn?.firstOrNull()
                )
            }
            Result.success(books)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getBookDetails(
        workKey: String,
        editionKey: String?
    ): Result<Book> {
        TODO("Not yet implemented")
    }

    private fun getCoverUrl(coverId: Int?): String? {
        return coverId?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" }
    }

}