package com.example.coinnews.ui.interest

import androidx.lifecycle.ViewModel
import com.example.coinnews.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterestCoinListViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

}