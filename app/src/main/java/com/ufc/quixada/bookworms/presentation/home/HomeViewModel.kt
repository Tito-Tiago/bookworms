package com.ufc.quixada.bookworms.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.repository.BookResult
// --- IMPORTS CORRIGIDOS ---
import com.ufc.quixada.bookworms.domain.usecase.favorite.ManageFavoriteUseCase
import com.ufc.quixada.bookworms.domain.usecase.book.SaveBookUseCase
import com.ufc.quixada.bookworms.domain.usecase.book.SearchBooksUseCase // Assumindo estar junto com SaveBook
import com.ufc.quixada.bookworms.domain.usecase.user.SearchUsersUseCase // Corrected package
// --------------------------
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
    private val searchUsersUseCase: SearchUsersUseCase,
    private val manageFavoriteUseCase: ManageFavoriteUseCase,
    private val saveBookUseCase: SaveBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Controle de fluxo para a pesquisa (substitui o Job manual)
    private val queryFlow = MutableStateFlow("")

    init {
        observeQuery()
        observeFavorites()
        // Busca inicial para não deixar a tela vazia
        onSearchQueryChange("Harry Potter")
    }

    // --- LÓGICA DE PESQUISA ---

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(500) // Espera o usuário parar de digitar
                .collectLatest { query ->
                    performSearch(query, _uiState.value.searchType)
                }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
        queryFlow.value = newQuery
    }

    fun onSearchTypeChange(newType: SearchType) {
        if (_uiState.value.searchType != newType) {
            _uiState.update { it.copy(searchType = newType) }
            // Refaz a busca com o tipo novo e termo atual
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
                            // Em caso de erro na busca de usuários, apenas limpa a lista
                            _uiState.update { it.copy(isLoading = false, readers = emptyList()) }
                        }
                    )
                }
            }
        }
    }



    private fun observeFavorites() {
        viewModelScope.launch {
            manageFavoriteUseCase.observeFavorites()
                .collect { favoriteIds ->
                    _uiState.update { it.copy(favoriteBookIds = favoriteIds.toSet()) }
                }
        }
    }


    fun onBookSelected(book: Book, navigate: (String) -> Unit) {
        viewModelScope.launch {
            saveBookUseCase(book)
            navigate(book.bookId)
        }
    }
}