package com.ufc.quixada.bookworms.domain.usecase

import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.FollowRepository
import com.ufc.quixada.bookworms.domain.repository.FollowResult
import javax.inject.Inject

class ToggleFollowUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val followRepository: FollowRepository
) {
    suspend operator fun invoke(targetUserId: String, isCurrentlyFollowing: Boolean): FollowResult {
        val currentUser = authRepository.getCurrentUser()
            ?: return FollowResult.Error("Usuário não logado")

        return if (isCurrentlyFollowing) {
            followRepository.unfollowUser(currentUser.uid, targetUserId)
        } else {
            followRepository.followUser(currentUser.uid, targetUserId)
        }
    }
}