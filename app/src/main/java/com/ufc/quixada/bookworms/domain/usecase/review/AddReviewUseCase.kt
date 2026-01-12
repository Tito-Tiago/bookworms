package com.ufc.quixada.bookworms.domain.usecase.review

import com.ufc.quixada.bookworms.domain.model.Activity
import com.ufc.quixada.bookworms.domain.model.ActivityReferenceType
import com.ufc.quixada.bookworms.domain.model.Review
import com.ufc.quixada.bookworms.domain.repository.ActivityRepository
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.repository.ReviewRepository
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import com.ufc.quixada.bookworms.domain.repository.SingleReviewResult
import java.util.UUID
import javax.inject.Inject

class AddReviewUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository,
    private val activityRepository: ActivityRepository // Dependência adicionada
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

        // Cria o objeto Review
        val review = Review(
            reviewId = UUID.randomUUID().toString(),
            userId = currentUser.uid,
            bookId = bookId,
            nota = nota,
            userName = currentUser.nome,
            textoResenha = textoResenha,
            contemSpoiler = contemSpoiler
        )

        // Tenta salvar a review
        val result = reviewRepository.addReview(review)

        // Se salvou com sucesso, gera a atividade para o Feed
        if (result is SingleReviewResult.Success) {
            try {
                val activity = Activity(
                    userIdOrigem = currentUser.uid,
                    userIdDono = currentUser.uid,
                    tipoReferencia = ActivityReferenceType.REVIEW,
                    idReferencia = review.reviewId,
                    bookId = bookId,
                    tipoAtividade = "REVIEW",
                    descricao = "avaliou o livro com $nota estrelas" // Texto base para o feed
                )
                activityRepository.createActivity(activity)
            } catch (e: Exception) {
                e.printStackTrace()
                // Não falhamos o review se a atividade falhar, apenas logamos
            }
        }

        return result
    }
}