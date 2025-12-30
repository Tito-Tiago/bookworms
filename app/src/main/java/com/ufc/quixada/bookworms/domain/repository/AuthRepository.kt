package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.User

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

interface AuthRepository {
    suspend fun register(nome: String, email: String, password: String): AuthResult
    suspend fun login(email: String, password: String): AuthResult
    suspend fun loginWithGoogle(idToken: String): AuthResult
    suspend fun logout()
    suspend fun getCurrentUser(): User?
    fun isUserLoggedIn(): Boolean
}