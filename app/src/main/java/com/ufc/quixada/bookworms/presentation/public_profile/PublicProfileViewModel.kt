package com.ufc.quixada.bookworms.presentation.public_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.usecase.shelf.ManageShelfUseCase
import com.ufc.quixada.bookworms.domain.usecase.user.GetPublicUserProfileUseCase
import com.ufc.quixada.bookworms.domain.usecase.user.ToggleFollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPublicUserProfileUseCase: GetPublicUserProfileUseCase,
    private val toggleFollowUserUseCase: ToggleFollowUserUseCase,
    private val manageShelfUseCase: ManageShelfUseCase
) : ViewModel() {

    private val userId: String = checkNotNull(savedStateHandle["userId"])

    private val _uiState = MutableStateFlow(PublicProfileUiState())
    val uiState: StateFlow<PublicProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun refreshProfile() {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getPublicUserProfileUseCase(userId).fold(
                onSuccess = { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = data.user,
                            isFollowing = data.isFollowing,
                            followersCount = data.followersCount,
                            followingCount = data.followingCount,
                            isOwnProfile = data.isOwnProfile
                        )
                    }
                    refreshShelves()
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message)
                    }
                }
            )
        }
    }

    fun refreshShelves() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingShelves = true) }

            manageShelfUseCase.getUserShelves(userId).fold(
                onSuccess = { shelvesWithBooks ->
                    _uiState.update {
                        it.copy(isLoadingShelves = false, shelves = shelvesWithBooks)
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoadingShelves = false) }
                }
            )
        }
    }

    fun onToggleFollowClick() {
        viewModelScope.launch {
            // Captura o estado ATUAL antes de inverter a UI
            val currentFollowing = uiState.value.isFollowing

            // Atualização otimista da UI (inverte visualmente imediatamente)
            val newStatus = !currentFollowing

            _uiState.update {
                it.copy(
                    isFollowing = newStatus,
                    followersCount = if (newStatus) it.followersCount + 1 else it.followersCount - 1
                )
            }

            try {
                // CORREÇÃO: Passamos o userId E o status atual (currentFollowing)
                toggleFollowUserUseCase(userId, currentFollowing)
            } catch (e: Exception) {
                // Reverte a UI em caso de erro
                _uiState.update {
                    it.copy(
                        isFollowing = currentFollowing,
                        followersCount = if (currentFollowing) it.followersCount + 1 else it.followersCount - 1,
                        errorMessage = "Erro ao atualizar seguidor: ${e.message}"
                    )
                }
            }
        }
    }
}