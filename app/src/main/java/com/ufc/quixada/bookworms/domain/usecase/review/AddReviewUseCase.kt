package com.ufc.quixada.bookworms.domain.usecase.review

import com.ufc.quixada.bookworms.domain.model.Review
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.ReviewRepository
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import com.ufc.quixada.bookworms.domain.repository.SingleReviewResult
import com.ufc.quixada.bookworms.domain.repository.UserRepository
import java.util.UUID
import javax.inject.Inject

class AddReviewUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        bookId: String,
        nota: Int,
        textoResenha: String,
        contemSpoiler: Boolean
    ): SingleReviewResult {
        val bookResult = bookRepository.getBook(bookId)
        val currentUser = authRepository.getCurrentUser()

        if (bookResult is SingleBookResult.Error){
            return SingleReviewResult.Error("Erro ao buscar livro: ${bookResult.message}")
        }

        if (currentUser == null) {
            return SingleReviewResult.Error("Erro ao buscar usuário")
        }

        if (nota !in 1..5){
            return SingleReviewResult.Error("A nota deve estar entre 1 e 5")
        }

        //as datas já são preenchidas no própio model
        val review = Review(
            reviewId = UUID.randomUUID().toString(),
            userId = currentUser.uid,
            bookId = bookId,
            nota = nota,
            userName = currentUser.nome,
            textoResenha = textoResenha,
            contemSpoiler = contemSpoiler
        )

        return reviewRepository.addReview(review)
    }
}
