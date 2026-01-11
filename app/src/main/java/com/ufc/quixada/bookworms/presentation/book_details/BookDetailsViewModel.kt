package com.ufc.quixada.bookworms.presentation.book_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.model.ShelfType
import com.ufc.quixada.bookworms.domain.repository.FavoriteResult
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import com.ufc.quixada.bookworms.domain.usecase.GetBookDetailsUseCase
import com.ufc.quixada.bookworms.domain.usecase.ManageFavoriteUseCase
import com.ufc.quixada.bookworms.domain.usecase.ManageShelfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val getBookDetailsUseCase: GetBookDetailsUseCase,
    private val manageFavoriteUseCase: ManageFavoriteUseCase,
    private val manageShelfUseCase: ManageShelfUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()

    private val bookId: String? = savedStateHandle["bookId"]

    init {
        bookId?.let { loadBook(it) }
    }

    fun loadBook(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val bookResult = getBookDetailsUseCase(id)
            val favResult = manageFavoriteUseCase.checkFavoriteStatus(id)
            val shelfStatus = manageShelfUseCase.getStatus(id)

            if (bookResult is SingleBookResult.Success) {
                val isFav = if (favResult is FavoriteResult.Success) favResult.isFavorite else false

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        book = bookResult.data,
                        isFavorite = isFav,
                        shelfType = shelfStatus
                    )
                }
            } else if (bookResult is SingleBookResult.Error) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = bookResult.message)
                }
            }
        }
    }

    fun onFavoriteClick() {
        val currentId = bookId ?: return
        val currentStatus = _uiState.value.isFavorite

        viewModelScope.launch {
            _uiState.update { it.copy(isFavorite = !currentStatus) }

            val result = manageFavoriteUseCase.toggleFavorite(currentId, currentStatus)

            if (result is FavoriteResult.Error) {
                _uiState.update {
                    it.copy(
                        isFavorite = currentStatus,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun onShelfSelected(shelfType: ShelfType) {
        val currentId = bookId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = manageShelfUseCase(currentId, shelfType)

            result.onSuccess {
                _uiState.update { it.copy(shelfType = shelfType, isLoading = false, errorMessage = null) }
            }.onFailure { exception ->
                android.util.Log.e("ShelfError", "Falha ao salvar na estante", exception)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Erro: ${exception.localizedMessage}"
                    )
                }
            }
        }
    }
}