package com.ufc.quixada.bookworms.domain.model

sealed class AuthResult {
    data class Success(val uid: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}