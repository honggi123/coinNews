package com.example.coinnews.ui.coinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.coinnews.data.repository.NewsRepository
import com.example.coinnews.model.Coin
import com.example.coinnews.model.Ordering
import com.example.coinnews.model.Sort
import com.example.coinnews.model.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _coins = MutableStateFlow<PagingData<Coin>?>(null)
    val coins = _coins.filterNotNull()

    private val _selectedSort = MutableStateFlow<Sort?>(null)
    val selectedSort = _selectedSort

    init {
        refreshCoins(
            Sort(SortOption.Price, Ordering.Descending)
        )
    }

    fun onSortClick(sort: Sort) {
        val newOrdering = getNextOrdering(sort.ordering)
        val newSort = sort.copy(ordering = newOrdering)

        refreshCoins(newSort)
    }

    private fun refreshCoins(sort: Sort) {
        viewModelScope.launch {
            // todo result
            _coins.value = repository.getCoins(sort).cachedIn(viewModelScope).first()
            _selectedSort.value = sort
        }
    }

    private fun getNextOrdering(ordering: Ordering): Ordering {
        return when (ordering) {
            Ordering.None -> Ordering.Ascending
            Ordering.Ascending -> Ordering.Descending
            Ordering.Descending -> Ordering.None
        }
    }
}