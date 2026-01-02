package com.ufc.quixada.bookworms.domain.usecase

import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.OpenLibraryRepository
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import javax.inject.Inject

class GetBookDetailsUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val openLibraryRepository: OpenLibraryRepository
) {
    suspend operator fun invoke(bookId: String): SingleBookResult {
        val result = bookRepository.getBook(bookId)

        if (result is SingleBookResult.Success) {
            val book = result.data

            if (book.sinopse.isNotBlank()) {
                return result
            }

            val apiResult = openLibraryRepository.getBookDetails(bookId)

            if (apiResult.isSuccess) {
                val apiDetails = apiResult.getOrThrow()

                val updatedBook = book.copy(
                    sinopse = apiDetails.sinopse,
                    notaApiExterna = apiDetails.notaApiExterna ?: book.notaApiExterna,
                    isbn = apiDetails.isbn ?: book.isbn
                )

                bookRepository.saveBook(updatedBook)
                return SingleBookResult.Success(updatedBook)
            }
        }

        return result
    }
}