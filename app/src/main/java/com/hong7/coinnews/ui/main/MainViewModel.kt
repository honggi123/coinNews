package com.hong7.coinnews.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hong7.coinnews.data.repository.FilterRepository
import com.hong7.coinnews.model.Coin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val filterRepository: FilterRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        _isLoading.value = true
        viewModelScope.launch {
            filterRepository.getUserFilter().collectLatest { filter ->
                if (filter == null) {
                    filterRepository.addCoinsFilter(
                        listOf(
                            Coin(
                                id = "crypto",
                                name = "암호화폐",
                                symbol = ""
                            )
                        )
                    )
                }
                _isLoading.value = false

            }
        }
    }
}
