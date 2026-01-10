package com.ufc.quixada.bookworms.domain.usecase

import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.ReviewRepository
import com.ufc.quixada.bookworms.domain.repository.ReviewResult
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import javax.inject.Inject

class GetBookReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val bookRepository: BookRepository
) {
    suspend fun invoke(bookId: String): ReviewResult {
        val bookResult = bookRepository.getBook(bookId)

        if (bookResult is SingleBookResult.Error){
            return ReviewResult.Error("Erro ao buscar livro ${bookResult.message}")
        }

        return reviewRepository.getReviewsByBook(bookId)
    }
}