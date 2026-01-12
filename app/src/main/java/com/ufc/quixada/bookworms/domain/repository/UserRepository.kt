package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.User

sealed class UserResult {
    data class Success(val data: User) : UserResult()
    data class Error(val message: String) : UserResult()
}

interface UserRepository {
    suspend fun getUser(uid: String): UserResult
    suspend fun updateUser(user: User): UserResult

    suspend fun searchUsers(query: String): List<User>
}