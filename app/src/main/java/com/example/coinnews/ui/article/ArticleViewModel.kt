package com.example.coinnews.ui.article

import androidx.lifecycle.ViewModel
import com.example.coinnews.data.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ArticleUiState(
    val loading: Boolean = false,
    val errorMessage: String = ""
)

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArticleUiState(loading = true))
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

}
