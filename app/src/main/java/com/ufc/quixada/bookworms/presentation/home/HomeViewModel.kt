package com.ufc.quixada.bookworms.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.repository.BookResult
import com.ufc.quixada.bookworms.domain.usecase.GetBooksUseCase
import com.ufc.quixada.bookworms.domain.usecase.ManageFavoriteUseCase
import com.ufc.quixada.bookworms.domain.usecase.SearchBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val searchBooksUseCase: SearchBooksUseCase,
    private val manageFavoriteUseCase: ManageFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadBooks()
        observeFavorites()
    }

    // Método para observar o fluxo em tempo real
    private fun observeFavorites() {
        viewModelScope.launch {
            manageFavoriteUseCase.observeFavorites()
                .collect { favoriteIds ->
                    _uiState.update { it.copy(favoriteBookIds = favoriteIds.toSet()) }
                }
        }
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val bookResult = getBooksUseCase()) {
                is BookResult.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false, books = bookResult.data)
                    }
                }
                is BookResult.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = bookResult.message)
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            performSearch(newQuery)
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        when (val result = searchBooksUseCase(query)) {
            is BookResult.Success -> {
                _uiState.update { it.copy(isLoading = false, books = result.data) }
            }
            is BookResult.Error -> {
                _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
            }
        }
    }
}