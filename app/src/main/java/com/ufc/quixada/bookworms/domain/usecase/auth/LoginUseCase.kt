package com.ufc.quixada.bookworms.domain.usecase.auth

import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.AuthResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        if (email.isBlank()) {
            return AuthResult.Error("E-mail não pode estar vazio")
        }

        if (password.isBlank()) {
            return AuthResult.Error("Senha não pode estar vazia")
        }

        return authRepository.login(email, password)
    }
}