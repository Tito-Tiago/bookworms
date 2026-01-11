package com.ufc.quixada.bookworms.domain.usecase.user

import com.ufc.quixada.bookworms.domain.model.User
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.FollowRepository
import com.ufc.quixada.bookworms.domain.repository.FollowResult
import com.ufc.quixada.bookworms.domain.repository.UserRepository
import com.ufc.quixada.bookworms.domain.repository.UserResult
import javax.inject.Inject

class ToggleFollowUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val followRepository: FollowRepository
) {
    // Retorna true se passou a seguir, false se deixou de seguir
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

class GetPublicUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val authRepository: AuthRepository
) {
    data class PublicProfileData(
        val user: User,
        val isFollowing: Boolean,
        val followersCount: Int,
        val followingCount: Int
    )

    suspend operator fun invoke(targetUserId: String): Result<PublicProfileData> {
        val currentUser = authRepository.getCurrentUser()

        return try {
            val userResult = userRepository.getUser(targetUserId)
            if (userResult is UserResult.Success) {
                val isFollowing = currentUser?.let {
                    followRepository.isFollowing(it.uid, targetUserId)
                } ?: false

                val followers = followRepository.getFollowersCount(targetUserId)
                val following = followRepository.getFollowingCount(targetUserId)

                Result.success(PublicProfileData(userResult.data, isFollowing, followers, following))
            } else {
                Result.failure(Exception("Usuário não encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}