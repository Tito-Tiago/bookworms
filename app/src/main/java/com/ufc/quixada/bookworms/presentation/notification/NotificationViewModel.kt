package com.ufc.quixada.bookworms.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import com.ufc.quixada.bookworms.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            if (user != null) {
                _uiState.update { it.copy(isLoading = true) }
                notificationRepository.getNotifications(user.uid)
                    .catch { e ->
                        _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                    }
                    .collect { notifications ->
                        _uiState.update { 
                            it.copy(
                                isLoading = false, 
                                notifications = notifications
                            ) 
                        }
                    }
            } else {
                 _uiState.update { it.copy(errorMessage = "Usuário não logado") }
            }
        }
    }
}