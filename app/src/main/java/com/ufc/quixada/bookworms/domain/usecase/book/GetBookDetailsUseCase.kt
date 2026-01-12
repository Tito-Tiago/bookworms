package com.ufc.quixada.bookworms.domain.usecase.book

import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.OpenLibraryRepository
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import javax.inject.Inject

class GetBookDetailsUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val openLibraryRepository: OpenLibraryRepository
) {
    suspend operator fun invoke(bookId: String): SingleBookResult {

        val apiResult = openLibraryRepository.getBookDetails(bookId)

        if (apiResult.isSuccess) {
            val apiBook = apiResult.getOrThrow()
            val localResult = bookRepository.getBook(bookId)
            val existingBook = if (localResult is SingleBookResult.Success) localResult.data else null

            val finalBook = if (existingBook != null) {
                existingBook.copy(
                    sinopse = if (apiBook.sinopse.isNotBlank()) apiBook.sinopse else existingBook.sinopse,
                    notaApiExterna = apiBook.notaApiExterna ?: existingBook.notaApiExterna,
                    isbn = apiBook.isbn ?: existingBook.isbn,
                    capaUrl = if (!apiBook.capaUrl.isNullOrBlank()) apiBook.capaUrl else existingBook.capaUrl,
                    titulo = if (apiBook.titulo.isNotBlank()) apiBook.titulo else existingBook.titulo,
                    autor = if (apiBook.autor.isNotBlank() && apiBook.autor != "Autor Desconhecido") apiBook.autor else existingBook.autor
                )
            } else {
                apiBook
            }
            bookRepository.saveBook(finalBook)
            return SingleBookResult.Success(finalBook)
        }

        return bookRepository.getBook(bookId)
    }
}