package com.ufc.quixada.bookworms.domain.usecase

import com.ufc.quixada.bookworms.domain.model.User
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.FollowRepository
import com.ufc.quixada.bookworms.domain.repository.UserRepository
import com.ufc.quixada.bookworms.domain.repository.UserResult
import javax.inject.Inject

class GetPublicUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val authRepository: AuthRepository
) {
    data class PublicProfileData(
        val user: User,
        val isFollowing: Boolean,
        val isOwnProfile: Boolean,
        val followersCount: Int,
        val followingCount: Int
    )

    suspend operator fun invoke(targetUserId: String): Result<PublicProfileData> {
        val currentUser = authRepository.getCurrentUser()

        return try {
            val userResult = userRepository.getUser(targetUserId)
            if (userResult is UserResult.Success) {
                val isOwnProfile = currentUser?.uid == targetUserId

                val isFollowing = currentUser?.let {
                    if (isOwnProfile) false else followRepository.isFollowing(it.uid, targetUserId)
                } ?: false

                val followers = followRepository.getFollowersCount(targetUserId)
                val following = followRepository.getFollowingCount(targetUserId)

                Result.success(PublicProfileData(userResult.data, isFollowing, isOwnProfile, followers, following))
            } else {
                Result.failure(Exception("Usuário não encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}