package com.ufc.quixada.bookworms.domain.usecase.book

import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.BookResult
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(): BookResult {
        return bookRepository.getBooks()
    }
}