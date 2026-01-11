package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.Follow

sealed class FollowResult {
    object Success : FollowResult()
    data class Error(val message: String) : FollowResult()
}

interface FollowRepository {
    suspend fun followUser(currentUserId: String, targetUserId: String): FollowResult
    suspend fun unfollowUser(currentUserId: String, targetUserId: String): FollowResult
    suspend fun isFollowing(currentUserId: String, targetUserId: String): Boolean
    suspend fun getFollowersCount(userId: String): Int
    suspend fun getFollowingCount(userId: String): Int
}