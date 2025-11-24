package com.ufc.quixada.bookworms.presentation.profile

import com.ufc.quixada.bookworms.domain.model.User

data class ProfileUiState(
    val user: User = User(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isLoggedOut: Boolean = false
)