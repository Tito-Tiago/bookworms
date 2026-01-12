package com.ufc.quixada.bookworms.domain.usecase.feed

import com.ufc.quixada.bookworms.domain.model.Activity
import com.ufc.quixada.bookworms.domain.repository.ActivityRepository
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.FollowRepository
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val followRepository: FollowRepository,
    private val activityRepository: ActivityRepository
) {
    suspend operator fun invoke(): List<Activity> {
        val currentUser = authRepository.getCurrentUser() ?: return emptyList()

        // 1. Pegar lista de amigos seguidos
        val followedIds = followRepository.getFollowedUserIds(currentUser.uid)

        if (followedIds.isEmpty()) return emptyList()

        // 2. Pegar atividades desses usuários
        return activityRepository.getActivitiesFromUsers(followedIds)
    }
}