package com.ufc.quixada.bookworms.presentation.book_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.model.ShelfType
import com.ufc.quixada.bookworms.domain.repository.FavoriteResult
import com.ufc.quixada.bookworms.domain.repository.ReviewResult
import com.ufc.quixada.bookworms.domain.repository.SingleBookResult
import com.ufc.quixada.bookworms.domain.repository.SingleReviewResult
import com.ufc.quixada.bookworms.domain.usecase.book.GetBookDetailsUseCase
import com.ufc.quixada.bookworms.domain.usecase.favorite.ManageFavoriteUseCase
import com.ufc.quixada.bookworms.domain.usecase.review.AddReviewUseCase
import com.ufc.quixada.bookworms.domain.usecase.review.GetBookReviewsUseCase
import com.ufc.quixada.bookworms.domain.usecase.shelf.ManageShelfUseCase
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
    private val addReviewUseCase: AddReviewUseCase,
    private val getBookReviewsUseCase: GetBookReviewsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailsUiState())
    val uiState: StateFlow<BookDetailsUiState> = _uiState.asStateFlow()

    private val bookId: String? = savedStateHandle["bookId"]

    init {
        bookId?.let {
            loadBook(it)
            loadReviews(it)
        }
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

    fun loadReviews(bookId: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val reviewsResult = getBookReviewsUseCase(bookId = bookId)

            if (reviewsResult is ReviewResult.Success) {
                _uiState.update {
                    it.copy(
                        reviews = reviewsResult.data,
                        isLoading = false
                    )
                }
            } else if (reviewsResult is ReviewResult.Error) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = reviewsResult.message
                    )
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

    fun onTextoResenhaChanged(newText: String) {
        _uiState.update {
            it.copy(textoResenha = newText)
        }
    }

    fun toggleContemSpoiler() {
        val contemSpoiler = _uiState.value.contemSpoiler

        _uiState.update {
            it.copy(contemSpoiler = !contemSpoiler)
        }
    }

    fun onRatingChanged(newRating: Int) {
        _uiState.update {
            it.copy(nota = newRating)
        }
    }

    fun onFazerResenhaClick() {
        val bookId = bookId ?: return //talves era massa colocar um erro
        val currentRating = uiState.value.nota

        if (currentRating < 1) {
            _uiState.update {
                it.copy(
                    errorMessage = "Selecione uma nota entre 1 e 5 antes de enviar a resenha"
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = addReviewUseCase(
                bookId = bookId,
                nota = uiState.value.nota,
                textoResenha = uiState.value.textoResenha,
                contemSpoiler = uiState.value.contemSpoiler
            )

            when (result) {
                is SingleReviewResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            textoResenha = "",
                            nota = 0,
                            contemSpoiler = false
                        )
                    }
                }
                is SingleReviewResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
}