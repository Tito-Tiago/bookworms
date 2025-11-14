package com.ufc.quixada.bookworms.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.model.AuthResult
import com.ufc.quixada.bookworms.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onRegisterClick(email: String, pass: String) {
        if (email.isBlank() || pass.length < 8) {
            _uiState.value = AuthUiState(error = "E-mail inválido ou senha muito curta (mín. 8).")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            when (val result = registerUseCase(email, pass)) {
                is AuthResult.Success -> {
                    _uiState.value = AuthUiState(isLoading = false, isSuccess = true)
                }
                is AuthResult.Error -> {
                    _uiState.value = AuthUiState(isLoading = false, error = result.message)
                }
            }
        }
    }
}