package com.ufc.quixada.bookworms.domain.usecase.review

import com.ufc.quixada.bookworms.domain.model.Review
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.ReviewRepository
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import com.ufc.quixada.bookworms.domain.repository.SingleReviewResult
import java.util.UUID
import javax.inject.Inject

class AddReviewUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(
        userId: String,
        bookId: String,
        nota: Int,
        textoResenha: String,
        contemSpoiler: Boolean
    ): SingleReviewResult {
        val bookResult = bookRepository.getBook(bookId)

        if (bookResult is SingleBookResult.Error){
            return SingleReviewResult.Error("Erro ao buscar livro: ${bookResult.message}")
        }

        if (nota !in 1..5){
            return SingleReviewResult.Error("A nota deve estar entre 1 e 5")
        }

        //as datas já são preenchidas no própio model
        val review = Review(
            reviewId = UUID.randomUUID().toString(),
            userId = userId,
            bookId = bookId,
            nota = nota,
            textoResenha = textoResenha,
            contemSpoiler = contemSpoiler
        )

        return reviewRepository.addReview(review)
    }
}
