package com.ufc.quixada.bookworms.presentation.auth.register

data class RegisterUiState(
    val nome: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisterSuccessful: Boolean = false
)