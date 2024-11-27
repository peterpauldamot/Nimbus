/*
 * Created by Peter Paul Damot on 11-28-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-28-2024.
 */

package com.weather.nimbus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.weather.nimbus.common.model.AppThemes
import com.weather.nimbus.presentation.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
): ViewModel() {
    private val _selectedTheme = MutableStateFlow(preferencesManager.getTheme())
    val selectedTheme: StateFlow<AppThemes> = _selectedTheme

    fun updateTheme(theme: AppThemes) {
        preferencesManager.saveTheme(theme)
        _selectedTheme.value = theme
    }
}