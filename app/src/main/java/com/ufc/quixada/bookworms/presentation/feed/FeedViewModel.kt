package com.ufc.quixada.bookworms.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufc.quixada.bookworms.domain.model.Activity
import com.ufc.quixada.bookworms.domain.model.Book
import com.ufc.quixada.bookworms.domain.repository.BookResult
import com.ufc.quixada.bookworms.domain.repository.BookRepository
import com.ufc.quixada.bookworms.domain.usecase.feed.GetFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedUiState(
    val isLoading: Boolean = false,
    val trendingBooks: List<Book> = emptyList(),
    val recentBooks: List<Book> = emptyList(),
    val activities: List<Activity> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val getFeedUseCase: GetFeedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadFeedData()
    }

    private fun loadFeedData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Carregar Trending
            val trendingResult = bookRepository.getTrendingBooks()
            val trending = if (trendingResult is BookResult.Success) trendingResult.data else emptyList()

            // Carregar Novidades
            val recentResult = bookRepository.getRecentBooks()
            val recent = if (recentResult is BookResult.Success) recentResult.data else emptyList()

            // Carregar Atividades
            val activities = getFeedUseCase()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    trendingBooks = trending,
                    recentBooks = recent,
                    activities = activities
                )
            }
        }
    }
}