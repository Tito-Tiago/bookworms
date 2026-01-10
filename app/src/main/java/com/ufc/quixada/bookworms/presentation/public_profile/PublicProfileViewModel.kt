package com.ufc.quixada.bookworms.presentation.public_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.model.User
import com.ufc.quixada.bookworms.domain.repository.FollowResult
import com.ufc.quixada.bookworms.domain.usecase.GetPublicUserProfileUseCase
import com.ufc.quixada.bookworms.domain.usecase.ToggleFollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PublicProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val isFollowing: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val errorMessage: String? = null
)

@HiltViewModel
class PublicProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, // Para pegar o ID da navegação
    private val getPublicUserProfileUseCase: GetPublicUserProfileUseCase,
    private val toggleFollowUserUseCase: ToggleFollowUserUseCase
) : ViewModel() {

    private val userId: String = checkNotNull(savedStateHandle["userId"])

    private val _uiState = MutableStateFlow(PublicProfileUiState())
    val uiState: StateFlow<PublicProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            getPublicUserProfileUseCase(userId).fold(
                onSuccess = { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = data.user,
                            isFollowing = data.isFollowing,
                            followersCount = data.followersCount,
                            followingCount = data.followingCount
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun onToggleFollowClick() {
        viewModelScope.launch {
            val currentFollowing = _uiState.value.isFollowing

            // Otimista: atualiza a UI antes da resposta do servidor
            _uiState.update {
                it.copy(
                    isFollowing = !currentFollowing,
                    followersCount = if (currentFollowing) it.followersCount - 1 else it.followersCount + 1
                )
            }

            when (val result = toggleFollowUserUseCase(userId, currentFollowing)) {
                is FollowResult.Success -> { /* Sucesso, estado já atualizado */ }
                is FollowResult.Error -> {
                    // Reverte em caso de erro
                    _uiState.update {
                        it.copy(
                            isFollowing = currentFollowing,
                            followersCount = if (currentFollowing) it.followersCount + 1 else it.followersCount - 1,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
}