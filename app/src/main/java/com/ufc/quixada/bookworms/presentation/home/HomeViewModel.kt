package com.ufc.quixada.bookworms.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.repository.BookResult
import com.ufc.quixada.bookworms.domain.usecase.SearchBooksUseCase
import com.ufc.quixada.bookworms.domain.usecase.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        observeQuery()
        // CORREÇÃO: Busca inicial ativada para não mostrar tela vazia
        onSearchQueryChange("Harry Potter")
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(500)
                .collectLatest { query ->
                    performSearch(query, _uiState.value.searchType)
                }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        // Atualiza a UI imediatamente com o texto digitado
        _uiState.update { it.copy(searchQuery = newQuery) }
        // Emite para o flow para disparar a busca com debounce
        queryFlow.value = newQuery
    }

    fun onSearchTypeChange(newType: SearchType) {
        if (_uiState.value.searchType != newType) {
            _uiState.update { it.copy(searchType = newType) }
            // Ao trocar de aba, refaz a busca com o termo atual
            performSearch(_uiState.value.searchQuery, newType)
        }
    }

    private fun performSearch(query: String, type: SearchType) {
        if (query.isBlank()) {
            _uiState.update { it.copy(books = emptyList(), readers = emptyList(), isLoading = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (type) {
                SearchType.BOOKS -> {
                    when (val result = searchBooksUseCase(query)) {
                        is BookResult.Success -> {
                            _uiState.update { it.copy(isLoading = false, books = result.data) }
                        }
                        is BookResult.Error -> {
                            _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                        }
                    }
                }
                SearchType.READERS -> {
                    searchUsersUseCase(query).fold(
                        onSuccess = { users ->
                            _uiState.update { it.copy(isLoading = false, readers = users) }
                        },
                        onFailure = {
                            _uiState.update { it.copy(isLoading = false, readers = emptyList()) }
                        }
                    )
                }
            }
        }
    }
}