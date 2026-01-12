package com.ufc.quixada.bookworms.domain.usecase.auth

import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}