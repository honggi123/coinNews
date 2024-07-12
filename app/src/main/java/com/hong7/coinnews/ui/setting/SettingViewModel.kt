package com.hong7.coinnews.ui.setting

import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.hong7.coinnews.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : ViewModel()