package com.ufc.quixada.bookworms.domain.usecase

import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.AuthResult
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(nome: String, email: String, password: String): AuthResult {
        if (nome.isBlank()) {
            return AuthResult.Error("Nome não pode estar vazio")
        }

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return AuthResult.Error("E-mail inválido")
        }

        if (password.length < 8) {
            return AuthResult.Error("A senha deve ter pelo menos 8 caracteres")
        }

        return authRepository.register(nome, email, password)
    }
}