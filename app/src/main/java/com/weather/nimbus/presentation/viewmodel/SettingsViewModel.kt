/*
 * Created by Peter Paul Damot on 11-28-2024
 * Copyright (c) 2024. All rights reserved.
 * Last modified on 11-28-2024.
 */

package com.weather.nimbus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.nimbus.common.model.AppThemes
import com.weather.nimbus.common.model.TemperatureUnit
import com.weather.nimbus.domain.settings.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCase: SettingsUseCase
): ViewModel() {
    private val _selectedTheme = MutableStateFlow<AppThemes>(AppThemes.DYNAMIC)
    val selectedTheme: StateFlow<AppThemes> = _selectedTheme

    private val _selectedTemperatureUnit = MutableStateFlow<TemperatureUnit>(TemperatureUnit.CELSIUS)
    val selectedTemperatureUnit: StateFlow<TemperatureUnit> = _selectedTemperatureUnit

    init {
        getSelectedTheme()
        getSelectedTemperatureUnit()
    }

    private fun getSelectedTheme() {
        viewModelScope.launch {
            _selectedTheme.value = settingsUseCase.getAppTheme()
        }
    }

    private fun getSelectedTemperatureUnit() {
        viewModelScope.launch {
            _selectedTemperatureUnit.value = settingsUseCase.getTemperatureUnits()
        }
    }

    fun updateTheme(theme: AppThemes) {
        viewModelScope.launch {
            settingsUseCase.updateAppTheme(theme)
            _selectedTheme.value = theme
        }
    }

    fun updateTemperatureUnits(temperatureUnit: TemperatureUnit) {
        viewModelScope.launch {
            settingsUseCase.updateTemperatureUnits(temperatureUnit)
            _selectedTemperatureUnit.value = temperatureUnit
        }
    }
}