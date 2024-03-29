package com.example.coinnews.ui.articlelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.data.repository.UserRepository
import com.example.coinnews.model.Coin
import com.example.coinnews.model.CoinFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val userFilters = userRepository.getFilters()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            emptyList()
        )

    val allFilters = userRepository.getAllFilters()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            emptyList()
        )

    private val _selectedFilter = MutableStateFlow<CoinFilter?>(null)
    val selectedFilter: StateFlow<CoinFilter?> = _selectedFilter.asStateFlow()

    val articles = selectedFilter
        .filterNotNull()    // todo
        .flatMapLatest { filter -> newsRepository.getArticles(filter) }
        .cachedIn(viewModelScope)

    fun onFilterClick(filter: CoinFilter) {
        // todo
        _selectedFilter.value = filter
    }

    fun updateNewFilters(filters: List<CoinFilter>) {
        viewModelScope.launch {
            userRepository.updateFilterSelect(filters)
        }
    }
}




