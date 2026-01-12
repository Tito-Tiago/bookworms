package com.ufc.quixada.bookworms.domain.usecase.review

import com.ufc.quixada.bookworms.domain.model.Review
import com.ufc.quixada.bookworms.domain.repository.ReviewRepository
import com.ufc.quixada.bookworms.domain.repository.SingleReviewResult
import java.util.Date
import javax.inject.Inject

class UpdateReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(
        reviewId: String,
        nota: Int,
        textoResenha: String,
        contemSpoiler: Boolean
    ): SingleReviewResult {
        //verifica se a review existe e guarda as informações antigas
        val oldReview = when(val reviewResult = reviewRepository.getReviewById(reviewId)) {
            is SingleReviewResult.Error -> {
                return SingleReviewResult.Error("Erro ao buscar review: ${reviewResult.message}")
            }
            is SingleReviewResult.Success -> {
                reviewResult.data
            }
        }

        if (nota !in 1..5){
            return SingleReviewResult.Error("A nota deve estar entre 1 e 5")
        }

        val updatedReview = Review(
            reviewId = oldReview.reviewId,
            userId = oldReview.userId,
            bookId = oldReview.bookId,
            nota = nota,
            userName = oldReview.userName,
            textoResenha = textoResenha,
            contemSpoiler = contemSpoiler,
            dataAtualizacao = Date()
        )

        return reviewRepository.updateReview(updatedReview)
    }
}