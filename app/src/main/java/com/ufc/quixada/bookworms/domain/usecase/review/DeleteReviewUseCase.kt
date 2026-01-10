package com.ufc.quixada.bookworms.domain.usecase.review

import com.ufc.quixada.bookworms.domain.repository.ReviewRepository
import com.ufc.quixada.bookworms.domain.repository.SingleReviewResult
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(reviewId: String): SingleReviewResult {
        val reviewResult = reviewRepository.getReviewById(reviewId)

        if (reviewResult is SingleReviewResult.Error) {
            return SingleReviewResult.Error("Erro ao buscar review: ${reviewResult.message}")
        }

        return reviewRepository.deleteReview(reviewId)
    }
}