package com.hong7.coinnews.ui.feature.bithumb

import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.CoinRepositoy
import com.hong7.coinnews.model.Coin
import com.hong7.coinnews.model.exception.ResponseResource
import com.hong7.coinnews.ui.base.BaseViewModel
import com.hong7.coinnews.ui.feature.market.model.Sort
import com.hong7.coinnews.ui.feature.market.model.SortCategory
import com.hong7.coinnews.ui.feature.market.model.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BithumbViewModel @Inject constructor(
    val coinRepositoy: CoinRepositoy,
) : BaseViewModel() {

    private val sortList = listOf(
        Sort(SortCategory.PRICE, SortType.NONE),
        Sort(SortCategory.PERCENTAGECHANGE, SortType.NONE),
        Sort(SortCategory.VOLUME, SortType.NONE)
    )
    private val _uiState = MutableStateFlow<MarketUiState>(MarketUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadMarketCoins()
    }

    fun refresh() = loadMarketCoins()

    fun onSortClick(selectedSortCategory: SortCategory, selectedSortType: SortType) {
        _uiState.value = when (val state = uiState.value) {
            is MarketUiState.Success -> {
                val nextSort = if (selectedSortType.order + 1 > SortType.values().size) {
                    SortType.getCurrentSortType(1)
                } else {
                    SortType.getCurrentSortType(selectedSortType.order + 1)
                }

                state.copy(
                    coinList = state.coinList.sort(selectedSortCategory, nextSort),
                    selectedSort = Sort(
                        sortCategory = selectedSortCategory,
                        sortType = nextSort
                    )
                )
            }

            else -> state
        }
    }

    fun List<Coin>.sort(sortCategory: SortCategory, sortType: SortType): List<Coin> {
        return when (sortType) {
            SortType.ASCENDING -> {
                when (sortCategory) {
                    SortCategory.PRICE -> this.sortedBy { it.tradePrice }
                    SortCategory.PERCENTAGECHANGE -> this.sortedBy { it.changeRate }
                    SortCategory.VOLUME -> this.sortedBy { it.accTradePrice24h }
                }
            }

            SortType.DESCENDING -> {
                when (sortCategory) {
                    SortCategory.PRICE -> this.sortedByDescending { it.tradePrice }
                    SortCategory.PERCENTAGECHANGE -> this.sortedByDescending { it.changeRate }
                    SortCategory.VOLUME -> this.sortedByDescending { it.accTradePrice24h }
                }
            }

            SortType.NONE -> {
                when (sortCategory) {
                    SortCategory.PRICE -> this
                    SortCategory.PERCENTAGECHANGE -> this
                    SortCategory.VOLUME -> this
                }
            }
        }
    }

    private var loadJob: Job? = null

    private fun loadMarketCoins() {
        loadJob = viewModelScope.launch {
            coinRepositoy.getBithumbCoins()
                .collectLatest { result ->
                    when (result) {
                        is ResponseResource.Success -> {
                            if (uiState.value is MarketUiState.Success) {
                                _uiState.value =
                                    (uiState.value as MarketUiState.Success).copy(
                                        LocalDateTime.now(),
                                        coinList = result.data
                                    )
                            } else {
                                _uiState.value =
                                    MarketUiState.Success(
                                        LocalDateTime.now(),
                                        sortList,
                                        sortList.first(),
                                        coinList = result.data
                                    )
                            }
                        }

                        is ResponseResource.Error -> onErrorResonse(result)
                        else -> Unit
                    }
                }
        }
    }
}

sealed class MarketUiState {

    object Loading : MarketUiState()

    data class Success(
        val updatedDateTime: LocalDateTime,
        val sortList: List<Sort>,
        val selectedSort: Sort,
        val coinList: List<Coin>
    ) : MarketUiState()

    data class Failed(
        val throwable: Throwable
    ) : MarketUiState()
}

