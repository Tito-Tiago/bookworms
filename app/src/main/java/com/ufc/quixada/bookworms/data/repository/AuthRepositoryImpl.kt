package com.ufc.quixada.bookworms.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.ufc.quixada.bookworms.domain.model.AuthResult
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementação concreta do AuthRepository.
 * É aqui que o Firebase Auth SDK é usado.
 */
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun register(email: String, pass: String): AuthResult {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            AuthResult.Success(result.user!!.uid)
        } catch (e: Exception) {
            // Converte a exceção do Firebase em uma mensagem de erro amigável
            AuthResult.Error(e.message ?: "Ocorreu um erro no cadastro.")
        }
    }

    override suspend fun login(email: String, pass: String): AuthResult {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            AuthResult.Success(result.user!!.uid)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "E-mail ou senha inválidos.")
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getUid(): String? {
        return auth.currentUser?.uid
    }

    override fun logout() {
        auth.signOut()
    }
}