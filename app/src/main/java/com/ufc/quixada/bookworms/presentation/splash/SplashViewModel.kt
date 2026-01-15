package com.ufc.quixada.bookworms.presentation.splash

import androidx.lifecycle.ViewModel
import com.ufc.quixada.bookworms.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}