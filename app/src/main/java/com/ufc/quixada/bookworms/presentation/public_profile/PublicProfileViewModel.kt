package com.ufc.quixada.bookworms.presentation.public_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.model.User
import com.ufc.quixada.bookworms.domain.repository.FollowRepository
import com.ufc.quixada.bookworms.domain.repository.FollowResult
import com.ufc.quixada.bookworms.domain.repository.ShelfRepository
import com.ufc.quixada.bookworms.domain.repository.ShelfWithBooks
import com.ufc.quixada.bookworms.domain.usecase.user.GetPublicUserProfileUseCase
import com.ufc.quixada.bookworms.domain.usecase.user.ToggleFollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PublicProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val isFollowing: Boolean = false,
    val isOwnProfile: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val shelves: List<ShelfWithBooks> = emptyList(),
    val isLoadingShelves: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class PublicProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPublicUserProfileUseCase: GetPublicUserProfileUseCase,
    private val toggleFollowUserUseCase: ToggleFollowUserUseCase,
    private val followRepository: FollowRepository,
    private val shelfRepository: ShelfRepository
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
                            isOwnProfile = data.isOwnProfile
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )

            launch {
                followRepository.getFollowersCountFlow(userId)
                    .catch { e ->
                        // Captura erro de permissão no logout e evita o crash
                        android.util.Log.e("PublicProfileVM", "Erro no flow de seguidores: ${e.message}")
                    }
                    .collect { count ->
                        _uiState.update { it.copy(followersCount = count) }
                    }
            }

            launch {
                followRepository.getFollowingCountFlow(userId)
                    .catch { e ->
                        // Captura erro de permissão no logout e evita o crash
                        android.util.Log.e("PublicProfileVM", "Erro no flow de seguindo: ${e.message}")
                    }
                    .collect { count ->
                        _uiState.update { it.copy(followingCount = count) }
                    }
            }

            loadUserShelves()
        }
    }

    private fun loadUserShelves() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingShelves = true) }

            shelfRepository.getUserShelvesWithBooks(userId).fold(
                onSuccess = { shelves ->
                    _uiState.update { it.copy(isLoadingShelves = false, shelves = shelves) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingShelves = false) }
                }
            )
        }
    }

    fun refreshShelves() {
        loadUserShelves()
    }

    fun onToggleFollowClick() {
        if (_uiState.value.isOwnProfile) return

        viewModelScope.launch {
            val currentFollowing = _uiState.value.isFollowing

            _uiState.update { it.copy(isFollowing = !currentFollowing) }

            val result = toggleFollowUserUseCase(userId, currentFollowing)

            if (result is FollowResult.Error) {
                _uiState.update {
                    it.copy(
                        isFollowing = currentFollowing,
                        errorMessage = result.message
                    )
                }
            }
        }
    }
}