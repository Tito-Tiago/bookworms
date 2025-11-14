package com.ufc.quixada.bookworms.domain.repository

import com.ufc.quixada.bookworms.domain.model.AuthResult

interface AuthRepository {
    suspend fun register(email: String, pass: String): AuthResult
    suspend fun login(email: String, pass: String): AuthResult
    fun isUserLoggedIn(): Boolean
    fun getUid(): String?
    fun logout()
}