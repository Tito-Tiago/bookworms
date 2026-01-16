package com.ufc.quixada.bookworms.presentation.public_profile

import com.ufc.quixada.bookworms.domain.model.User
import com.ufc.quixada.bookworms.domain.repository.ShelfWithBooks

data class PublicProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isLoadingShelves: Boolean = false,
    val isFollowing: Boolean = false,
    val isOwnProfile: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val shelves: List<ShelfWithBooks> = emptyList(),
    val errorMessage: String? = null
)