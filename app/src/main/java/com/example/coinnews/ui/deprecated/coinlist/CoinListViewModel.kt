//package com.example.coinnews.ui.deprecated.coinlist
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.paging.PagingData
//import androidx.paging.cachedIn
//import com.example.coinnews.data.repository.CoinRepository
//import com.example.coinnews.model.Coin
//import com.example.coinnews.model.Ordering
//import com.example.coinnews.model.Sort
//import com.example.coinnews.model.SortOption
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.filterNotNull
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.flatMapLatest
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@OptIn(ExperimentalCoroutinesApi::class)
//@HiltViewModel
//class CoinListViewModel @Inject constructor(
//    private val repository: CoinRepository
//) : ViewModel() {
//
//    private val _selectedSort = MutableStateFlow<Sort?>(null)
//    val selectedSort = _selectedSort
//
//    private var currentSort = MutableStateFlow(
//        Sort(SortOption.Price, Ordering.Descending)
//    )
//
//    val coins = currentSort
//        .flatMapLatest { sort -> repository.getCoins(sort) }
//        .cachedIn(viewModelScope)
//
//    fun onSortClick(sort: Sort) {
//        val newOrdering = getNextOrdering(sort.ordering)
//        val newSort = sort.copy(ordering = newOrdering)
//        currentSort.value = newSort
//        _selectedSort.value = newSort
//    }
//
//    private fun getNextOrdering(ordering: Ordering): Ordering {
//        return when (ordering) {
//            Ordering.None -> Ordering.Ascending
//            Ordering.Ascending -> Ordering.Descending
//            Ordering.Descending -> Ordering.None
//        }
//    }
//}