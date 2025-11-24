package com.ufc.quixada.bookworms.domain.usecase

import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.UserRepository
import com.ufc.quixada.bookworms.domain.repository.UserResult
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): UserResult {
        val currentUser = authRepository.getCurrentUser()
            ?: return UserResult.Error("Usuário não está logado")

        return userRepository.getUser(currentUser.uid)
    }
}