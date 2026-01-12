package com.ufc.quixada.bookworms.domain.usecase.review

import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.ReviewRepository
import com.ufc.quixada.bookworms.domain.repository.SimpleReviewResult
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import com.ufc.quixada.bookworms.domain.repository.SingleReviewResult
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val bookRepository: BookRepository
) {
    suspend operator fun invoke(reviewId: String, bookId: String): SimpleReviewResult {
        val reviewResult = reviewRepository.getReviewById(reviewId)
        val bookResult = bookRepository.getBook(bookId)

        if (reviewResult is SingleReviewResult.Error) {
            return SimpleReviewResult.Error("Erro ao buscar review: ${reviewResult.message}")
        }
        if (bookResult is SingleBookResult.Error) {
            return SimpleReviewResult.Error("Erro ao buscar livro: ${bookResult.message}")
        }

        return reviewRepository.deleteReview(reviewId, bookId)
    }
}