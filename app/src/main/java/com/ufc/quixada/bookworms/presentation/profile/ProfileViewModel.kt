package com.ufc.quixada.bookworms.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.repository.UserResult
import com.ufc.quixada.bookworms.domain.usecase.user.GetUserProfileUseCase
import com.ufc.quixada.bookworms.domain.usecase.auth.LogoutUseCase
import com.ufc.quixada.bookworms.domain.usecase.user.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getUserProfileUseCase()) {
                is UserResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, user = result.data) }
                }
                is UserResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(user = it.user.copy(nome = newName), successMessage = null) }
    }

    fun onBioChange(newBio: String) {
        _uiState.update { it.copy(user = it.user.copy(bio = newBio), successMessage = null) }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null, isSaved = false) }

            when (val result = updateProfileUseCase(_uiState.value.user)) {
                is UserResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            successMessage = "Perfil atualizado com sucesso!",
                            user = result.data,
                            isSaved = true
                        )
                    }
                }
                is UserResult.Error -> {
                    _uiState.update {
                        it.copy(isSaving = false, errorMessage = result.message)
                    }
                }
            }
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            logoutUseCase()
            _uiState.update { it.copy(isLoggedOut = true) }
        }
    }
}