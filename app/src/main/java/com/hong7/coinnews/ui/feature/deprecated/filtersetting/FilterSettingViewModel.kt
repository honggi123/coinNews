//package com.hong7.coinnews.ui.feature.filtersetting
//
//import androidx.lifecycle.viewModelScope
//import com.hong7.coinnews.model.Coin
//import com.hong7.coinnews.model.exception.ResponseResource
//import com.hong7.coinnews.ui.base.BaseViewModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.flatMapLatest
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class FilterSettingViewModel @Inject constructor(
//    private val filterRepository: FilterRepository,
//) : BaseViewModel() {
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    val uiState: StateFlow<FilterSettingUiState> = filterRepository.getFilter()
//        .flatMapLatest { filterResponse ->
//            filterRepository.getUserFilter().map {
//                when(filterResponse){
//                    is ResponseResource.Success -> {
//                        val selectedCoinIds = it?.coins?.map { it.id }?.toSet() ?: emptySet()
//                        val coins = filterResponse.data.coins.map { coin ->
//                            coin.copy(isSelected = coin.id in selectedCoinIds)
//                        }
//                        FilterSettingUiState.Success(coins)
//                    }
//                    is ResponseResource.Loading -> {
//                        FilterSettingUiState.Loading
//                    }
//                    is ResponseResource.Error -> {
//                        onErrorResonse(filterResponse)
//                        FilterSettingUiState.Failed(filterResponse.exception)
//                    }
//                }
//            }
//        }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5_000),
//            FilterSettingUiState.Loading
//        )
//
//    fun onSelectCompleted(selectedCoins: List<Coin>) {
//        viewModelScope.launch {
//            filterRepository.refreshCoinsFilter(selectedCoins)
//        }
//    }
//}
//
//sealed interface FilterSettingUiState {
//    object Loading : FilterSettingUiState
//
//    data class Success(
//        val items: List<Coin>
//    ) : FilterSettingUiState
//
//    data class Failed(
//        val throwable: Throwable
//    ) : FilterSettingUiState
//}
