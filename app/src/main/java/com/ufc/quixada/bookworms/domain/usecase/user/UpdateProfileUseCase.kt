package com.ufc.quixada.bookworms.domain.usecase.user

import com.ufc.quixada.bookworms.domain.model.User
import com.ufc.quixada.bookworms.domain.repository.UserRepository
import com.ufc.quixada.bookworms.domain.repository.UserResult
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): UserResult {
        if (user.nome.isBlank()) {
            return UserResult.Error("O nome não pode ser vazio")
        }
        return userRepository.updateUser(user)
    }
}