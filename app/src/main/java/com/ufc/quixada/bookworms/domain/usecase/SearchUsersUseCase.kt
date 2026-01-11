package com.ufc.quixada.bookworms.domain.usecase

import com.ufc.quixada.bookworms.domain.model.User
import com.ufc.quixada.bookworms.domain.repository.UserRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(query: String): Result<List<User>> {
        return try {
            val users = userRepository.searchUsers(query.trim())
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}