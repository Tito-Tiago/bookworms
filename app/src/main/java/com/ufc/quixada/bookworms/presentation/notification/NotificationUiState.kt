package com.ufc.quixada.bookworms.presentation.notification

import com.ufc.quixada.bookworms.domain.model.Notification

data class NotificationUiState(
    val notifications: List<Notification> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)