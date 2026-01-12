package com.ufc.quixada.bookworms.domain.usecase.review

import com.ufc.quixada.bookworms.domain.repository.ReviewRepository
import com.ufc.quixada.bookworms.domain.repository.ReviewResult
import com.ufc.quixada.bookworms.domain.repository.UserRepository
import com.ufc.quixada.bookworms.domain.repository.UserResult
import javax.inject.Inject

class GetUserReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: String
    ): ReviewResult {
        val userResult = userRepository.getUser(userId)

        if (userResult is UserResult.Error) {
            return ReviewResult.Error("Erro ao buscar usuário: ${userResult.message}")
        }

        return reviewRepository.getReviewsByUser(userId)
    }
}