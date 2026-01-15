package com.ufc.quixada.bookworms.domain.usecase.review

import com.ufc.quixada.bookworms.domain.model.Activity
import com.ufc.quixada.bookworms.domain.model.ActivityReferenceType
import com.ufc.quixada.bookworms.domain.model.Notification
import com.ufc.quixada.bookworms.domain.model.Review
import com.ufc.quixada.bookworms.domain.repository.*
import com.ufc.quixada.bookworms.presentation.notification.NotificationHelper
import java.util.UUID
import javax.inject.Inject

class AddReviewUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository,
    private val activityRepository: ActivityRepository,
    private val followRepository: FollowRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationHelper: NotificationHelper
) {
    suspend operator fun invoke(
        bookId: String,
        nota: Int,
        textoResenha: String,
        contemSpoiler: Boolean
    ): SingleReviewResult {
        val bookResult = bookRepository.getBook(bookId)
        val currentUser = authRepository.getCurrentUser()

        if (bookResult is SingleBookResult.Error) return SingleReviewResult.Error(bookResult.message)
        if (currentUser == null) return SingleReviewResult.Error("Usuário não logado")

        val reviewId = UUID.randomUUID().toString()
        val review = Review(
            reviewId = reviewId,
            userId = currentUser.uid,
            bookId = bookId,
            nota = nota,
            userName = currentUser.nome,
            textoResenha = textoResenha,
            contemSpoiler = contemSpoiler
        )

        val result = reviewRepository.addReview(review)

        if (result is SingleReviewResult.Success) {
            val bookTitle = (bookResult as? SingleBookResult.Success)?.data?.titulo ?: "um livro"

            activityRepository.createActivity(
                Activity(
                    activityId = reviewId,
                    userIdOrigem = currentUser.uid,
                    userIdDono = currentUser.uid,
                    tipoReferencia = ActivityReferenceType.REVIEW,
                    idReferencia = reviewId,
                    bookId = bookId,
                    tipoAtividade = "REVIEW",
                    descricao = "avaliou o livro com $nota estrelas"
                )
            )

            val followers = followRepository.getFollowerUserIds(currentUser.uid)
            followers.forEach { followerId ->
                val title = "Nova análise de ${currentUser.nome}"
                val message = "Acabou de avaliar o livro $bookTitle"

                notificationRepository.sendNotification(
                    Notification(
                        id = "${followerId}_$reviewId",
                        userId = followerId,
                        senderId = currentUser.uid,
                        title = title,
                        message = message,
                        bookId = bookId
                    )
                )
                notificationHelper.showSystemNotification(title, message)
            }
        }
        return result
    }
}